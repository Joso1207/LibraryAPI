package ChasAcademy.LibraryAPI;

import ChasAcademy.LibraryAPI.persistence.model.Author;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.model.Loan;
import ChasAcademy.LibraryAPI.persistence.repository.AuthorRepository;
import ChasAcademy.LibraryAPI.persistence.repository.BookRepository;
import ChasAcademy.LibraryAPI.persistence.repository.LoanRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,properties = "security.enabled=false")
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class LoanControllerRestTemplateTests {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port + "/v1/api/loans";
    }

    @AfterEach
    void cleanup() {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    void shouldFetchLoan() {

        Author author = authorRepository.save(new Author("TestName"));
        Book book = bookRepository.save(new Book("HitchIt", author));
        Loan loan = loanRepository.save(new Loan(book));

        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/" + loan.getId(), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert response.getBody().contains("HitchIt");
    }

    @Test
    void shouldCreateLoan() {

        Author author = authorRepository.save(new Author("TestName"));
        Book book = bookRepository.save(new Book("HitchIt", author));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(
                book.getId().toString(),
                headers
        );

        ResponseEntity<String> response =
                restTemplate.postForEntity(baseUrl(), request, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assert response.getBody().contains("HitchIt");
    }

    @Test
    void shouldNotAllowSimultaneousLoansForSameBook() throws Exception {

        Author author = authorRepository.save(new Author("TestName"));
        Book book = bookRepository.save(new Book("HitchIt", author));

        String requestBody = book.getId().toString();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CyclicBarrier barrier = new CyclicBarrier(2);

        Callable<Integer> task = () -> {
            barrier.await();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request =
                    new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            baseUrl(),
                            request,
                            String.class
                    );

            return response.getStatusCode().value();
        };

        try {

            List<Future<Integer>> futures =
                    executor.invokeAll(List.of(task, task));

            List<Integer> statuses = new ArrayList<>();

            for (Future<Integer> future : futures) {
                statuses.add(future.get());
            }

            long successCount = statuses.stream()
                    .filter(s -> s == HttpStatus.CREATED.value())
                    .count();

            long conflictCount = statuses.stream()
                    .filter(s -> s == HttpStatus.CONFLICT.value())
                    .count();

            System.out.println("Statuses: " + statuses);
            System.out.println("Loan Count: " + loanRepository.count());

            // Core invariant:
            // never allow duplicate loans
            assertTrue(loanRepository.count() <= 1);

            // At most one successful creation
            assertTrue(successCount <= 1);

            // If one succeeded, the other should conflict
            if (successCount == 1) {
                assertEquals(1, conflictCount);
            }

            //Both requests should a combination of success and conflict.
            assertEquals(2, successCount + conflictCount);

        } finally {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}