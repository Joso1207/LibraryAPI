package ChasAcademy.LibraryAPI;


import ChasAcademy.LibraryAPI.api.core.dto.NewAuthorDTO;
import ChasAcademy.LibraryAPI.api.v1.controller.AuthorControllerv1;
import ChasAcademy.LibraryAPI.persistence.model.Author;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.repository.AuthorRepository;
import ChasAcademy.LibraryAPI.persistence.repository.BookRepository;
import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthorControllerTests {


    @Autowired
    private AuthorRepository repo;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){

    }

    @Test
    void authorCreation() throws Exception {
        //Arrange
        NewAuthorDTO newAuthor = NewAuthorDTO.builder()
                .name("newAuthor").build();
        MvcResult result = mockMvc.perform(post("/v1/api/authors")
                        .content(objectMapper.writeValueAsString(newAuthor))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newAuthor.name()))
                .andReturn();
        Number returnedId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        assertEquals(repo.findById(returnedId.longValue()).orElseThrow().getName(),newAuthor.name());
    }

    @Test
    void shouldFetchAuthor() throws Exception {
        //Arrange
        Author authorToFetch = repo.save(new Author("newAuthor"));
        //Act
        mockMvc.perform(get("/v1/api/authors/"+authorToFetch.getId()))
                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("newAuthor"));
    }

    @Test
    void authorNotFound() throws Exception {
        mockMvc.perform(get("/v1/api/authors/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowOnIllegalInputID() throws Exception {
        MvcResult result = mockMvc.perform(get("/v1/api/authors/9a99")).andReturn();
        assertEquals(MethodArgumentTypeMismatchException.class,result.getResolvedException().getClass());
        //assertEquals(400, result.getResponse().getStatus()); //MockMvc reads 500 as it bypasses default springboot handler
    }

    @Test
    void canFindWrittenWorks() throws Exception{
        Author author = repo.save(new Author("newAuthor"));
        Book newBook = bookRepo.save(new Book("HitchIt",author));

        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(get("/v1/api/authors/" + author.getId() + "/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)) // list size
                .andExpect(jsonPath("$[0].title").value("HitchIt"));
    }


}
