package ChasAcademy.LibraryAPI;

import ChasAcademy.LibraryAPI.api.core.dto.NewAuthorDTO;
import ChasAcademy.LibraryAPI.persistence.model.Author;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.repository.AuthorRepository;
import ChasAcademy.LibraryAPI.persistence.repository.BookRepository;
import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class AuthorControllerRestTemplateTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthorRepository repo;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private EntityManager entityManager;

    private String baseUrl() {
        return "http://localhost:" + port + "/v1/api/authors";
    }

    @BeforeEach
    void setup(){
        repo.deleteAll();
        bookRepo.deleteAll();
    }

    @Test
    void authorCreation() {

        NewAuthorDTO newAuthor = NewAuthorDTO.builder()
                .name("newAuthor")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<NewAuthorDTO> request = new HttpEntity<>(newAuthor, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(baseUrl(), request, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().contains("newAuthor"));

        Number returnedId = JsonPath.read(response.getBody(), "$.id");

        assertEquals(
                repo.findById(returnedId.longValue()).orElseThrow().getName(),
                newAuthor.name()
        );
    }


    @Test
    void shouldFetchAuthor() {

        Author authorToFetch = repo.save(new Author("newAuthor"));

        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/" + authorToFetch.getId(), String.class);

        System.out.println(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("newAuthor"));
    }

    @Test
    void authorNotFound() {

        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/999", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldThrowOnIllegalInputID() {

        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/9a99", String.class);

        // With TestRestTemplate we test HTTP behavior, not internal exceptions
        assertTrue(
                response.getStatusCode().is4xxClientError()
                        || response.getStatusCode().is5xxServerError()
        );
    }

    @Test
    void canFindWrittenWorks() {

        Author author = repo.save(new Author("newAuthor"));
        Book newBook = bookRepo.save(new Book("HitchIt", author));


        ResponseEntity<String> response =
                restTemplate.getForEntity(
                        baseUrl() + "/" + author.getId() + "/books",
                        String.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        int length = JsonPath.read(response.getBody(), "$.length()");
        String title = JsonPath.read(response.getBody(), "$[0].title");

        assertEquals(1, length);
        assertEquals("HitchIt", title);
    }
}