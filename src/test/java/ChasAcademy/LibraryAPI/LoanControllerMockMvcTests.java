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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
public class LoanControllerMockMvcTests {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MockMvc mockMvc;


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

}
