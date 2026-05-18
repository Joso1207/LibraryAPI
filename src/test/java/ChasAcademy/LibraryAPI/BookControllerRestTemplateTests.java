package ChasAcademy.LibraryAPI;

import ChasAcademy.LibraryAPI.api.core.dto.AuthorReferenceDTO;
import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.dto.UpdateBookRequestDTO;
import ChasAcademy.LibraryAPI.persistence.model.Author;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.repository.AuthorRepository;
import ChasAcademy.LibraryAPI.persistence.repository.BookRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import tools.jackson.databind.ObjectMapper;

import java.time.Year;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,properties = "security.enabled=false")
@AutoConfigureTestRestTemplate
@Import(TestSecurityConfig.class)
public class BookControllerRestTemplateTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository repo;

    @Autowired
    private AuthorRepository authorRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port + "/v2/api/books";
    }

    @AfterEach
    void cleanup() {
        repo.deleteAll();
        authorRepo.deleteAll();
    }


    @Test
    public void shouldCreateBook() throws Exception {

        AuthorReferenceDTO author = AuthorReferenceDTO.builder()
                .name("Author")
                .build();

        NewBookRequestDTO requestDTO = NewBookRequestDTO.builder()
                .title("Title")
                .isbn("9781566199094")
                .yearPublished(Year.of(1993))
                .author(author)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<>(objectMapper.writeValueAsString(requestDTO), headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(baseUrl(), request, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String body = response.getBody();

        assertEquals("Title", JsonPath.read(body, "$.data.title"));

        Number returnedId = JsonPath.read(body, "$.data.id");

        assertEquals(
                requestDTO.title(),
                repo.findById(returnedId.longValue()).orElseThrow().getTitle()
        );
    }


    @Test
    public void shouldGetAllBooks() {

        Author author =authorRepo.save(new Author("Author"));
        repo.save(new Book("Book1",author));

        Author author2 = authorRepo.save(new Author("Author2"));
        repo.save(new Book("Book2", author2));

        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl(), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        String body = response.getBody();

        assertEquals("Book1", JsonPath.read(body, "$.data[0].title"));
    }


    @Test
    public void shouldGetBookById() {

        Author author = new Author("Author");
        Book book = repo.save(new Book("HitchIt", authorRepo.save(author)));

        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/" + book.getId(), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals("HitchIt", JsonPath.read(response.getBody(), "$.data.title"));
    }

    @Test
    public void shouldUpdateBook() throws Exception {

        Author author = new Author("Author");
        Book book = repo.save(new Book("OldTitle", authorRepo.save(author)));

        UpdateBookRequestDTO updateDTO = UpdateBookRequestDTO.builder()
                .title("UpdatedTitle")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<>(objectMapper.writeValueAsString(updateDTO), headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        baseUrl() + "/" + book.getId(),
                        HttpMethod.PATCH,
                        request,
                        String.class
                );



        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UpdatedTitle", JsonPath.read(response.getBody(), "$.data.title"));

        assertEquals(
                "UpdatedTitle",
                repo.findById(book.getId()).orElseThrow().getTitle()
        );
    }


    @Test
    public void shouldDeleteBook() {

        Author author = new Author("Author");
        Book book = repo.save(new Book("ToDelete", authorRepo.save(author)));

        restTemplate.delete(baseUrl() + "/" + book.getId());

        assertFalse(repo.findById(book.getId()).isPresent());
    }


    @Test
    void shouldHandleConcurrentPatchRequests_withAcceptableFailures() throws Exception {

        // given
        Author author = authorRepo.save(new Author("Author"));
        Book book = repo.save(new Book("Original Title", author));

        int threads = 100;
        ExecutorService executor = Executors.newFixedThreadPool(20);
        CountDownLatch latch = new CountDownLatch(threads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        // when
        for (int i = 0; i < threads; i++) {
            int index = i;

            executor.submit(() -> {
                try {
                    UpdateBookRequestDTO dto = UpdateBookRequestDTO.builder()
                            .title("Title-" + index)
                            .build();

                    HttpEntity<UpdateBookRequestDTO> request =
                            new HttpEntity<>(dto);

                    ResponseEntity<String> response =
                            restTemplate.exchange(
                                    baseUrl() + "/" + book.getId(),
                                    HttpMethod.PATCH,
                                    request,
                                    String.class
                            );

                    if (response.getStatusCode().is2xxSuccessful()) {
                        successCount.incrementAndGet();
                    } else if (response.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT)){
                        failureCount.incrementAndGet();
                    }

                } catch (Exception e) {
                    failureCount.incrementAndGet();
                    System.out.println("FAILED: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // then

        System.out.println("Success: " + successCount.get());
        System.out.println("Failures: " + failureCount.get());

        //Majority shouold succeed
        assertTrue(successCount.get() > 80);

        //Total requests accounted for
        assertEquals(threads, successCount.get() + failureCount.get());

        //Final state is consistent
        Book updated = repo.findById(book.getId()).orElseThrow();

        System.out.println("Final title: " + updated.getTitle());

    }


}