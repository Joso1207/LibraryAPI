package ChasAcademy.LibraryAPI;

import ChasAcademy.LibraryAPI.persistence.model.Author;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.model.Loan;
import ChasAcademy.LibraryAPI.persistence.repository.AuthorRepository;
import ChasAcademy.LibraryAPI.persistence.repository.BookRepository;
import ChasAcademy.LibraryAPI.persistence.repository.LoanRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
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
        // Arrange
        Author author = authorRepository.save(new Author("TestName"));
        Book book = bookRepository.save(new Book("HitchIt", author));

        String requestBody = book.getId().toString();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Ensures both threads start at the same time
        CyclicBarrier barrier = new CyclicBarrier(2);

        Callable<Integer> task = () -> {
            barrier.await(); // synchronize start

            return mockMvc.perform(post("/v1/api/loans")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andReturn()
                    .getResponse()
                    .getStatus();
        };

        // Act
        List<Future<Integer>> futures = executor.invokeAll(List.of(task, task));

        // Collect results
        List<Integer> statuses = new ArrayList<>();
        for (Future<Integer> future : futures) {
            System.out.println(future.get());
            statuses.add(future.get());
        }

        executor.shutdown();

        // Assert
        long successCount = statuses.stream().filter(s -> s == HttpStatus.CREATED.value()).count();
        long conflictCount = statuses.stream().filter(s -> s == HttpStatus.BAD_REQUEST.value()).count();

        assertEquals(1,successCount);
        assertEquals(1,conflictCount);

        assertEquals(1,loanRepository.count());
    }

}
