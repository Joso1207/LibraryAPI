package ChasAcademy.LibraryAPI;

import ChasAcademy.LibraryAPI.persistence.model.Author;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.model.Loan;
import ChasAcademy.LibraryAPI.persistence.repository.AuthorRepository;
import ChasAcademy.LibraryAPI.persistence.repository.BookRepository;
import ChasAcademy.LibraryAPI.persistence.repository.LoanRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LoanControllerTests {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @AfterEach
    void cleanup(){
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    void shouldFetchLoan() throws Exception {
        //Arrange
        Author newAuthor = authorRepository.save(new Author("TestName"));
        Book bookToLoan = bookRepository.save(new Book("HitchIt",newAuthor));
        Loan loanToFetch = loanRepository.save(new Loan(bookToLoan));


        //Act
        mockMvc.perform(get("/v1/api/loans/"+loanToFetch.getId()))
                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookTitle").value("HitchIt"));
    }

    @Test
    void shouldCreateLoan() throws Exception{
        Author newAuthor = authorRepository.save(new Author("TestName"));
        Book bookToLoan = bookRepository.save(new Book("HitchIt",newAuthor));

        mockMvc.perform(post("/v1/api/loans")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(bookToLoan.getId().toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookTitle").value("HitchIt"));

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

            return mockMvc.perform(post("/v1/api/loans")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andReturn()
                    .getResponse()
                    .getStatus();
        };

        try {
            List<Future<Integer>> futures = executor.invokeAll(List.of(task, task));

            List<Integer> statuses = new ArrayList<>();
            for (Future<Integer> f : futures) {
                Integer status = f.get();
                statuses.add(status);
            }

            long successCount = statuses.stream()
                    .filter(s -> s == HttpStatus.CREATED.value())
                    .count();

            long conflictCount = statuses.stream()
                    .filter(s -> s == HttpStatus.CONFLICT.value())
                    .count();

            assertEquals(1, successCount);
            assertEquals(1, conflictCount);

            assertEquals(1, loanRepository.count());

        } finally {
            executor.shutdown();
            executor.awaitTermination(2, TimeUnit.SECONDS);
        }
    }

}
