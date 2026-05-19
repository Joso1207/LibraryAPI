package ChasAcademy.LibraryAPI;

import ChasAcademy.LibraryAPI.api.core.dto.AuthorReferenceDTO;
import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.dto.UpdateBookRequestDTO;
import ChasAcademy.LibraryAPI.api.v2.mapper.BookMapperV2;
import ChasAcademy.LibraryAPI.persistence.model.Author;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.repository.AuthorRepository;
import ChasAcademy.LibraryAPI.persistence.repository.BookRepository;
import ChasAcademy.LibraryAPI.service.BookService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;


import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@Transactional
public class BookControllerMockMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService service;

    @Autowired
    private BookRepository repo;

    @Autowired
    private AuthorRepository authorRepo;

    @Autowired
    private BookMapperV2 mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCreateBook() throws Exception {

        AuthorReferenceDTO author = AuthorReferenceDTO.builder().name("Author").build();

        NewBookRequestDTO requestDTO =  NewBookRequestDTO.builder()
                .title("Title")
                .isbn("9781566199094")
                .yearPublished(Year.of(1993))
                .author(author)
                .build();

        MvcResult result = mockMvc.perform(post("/v2/api/books")
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value(requestDTO.title()))
                .andReturn();


        Number returnedId = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");
        assertEquals(repo.findById(returnedId.longValue()).orElseThrow().getTitle(),requestDTO.title());
    }


    @Test
    public void shouldGetAllBooks() throws Exception {

        Author author = authorRepo.save(new Author("Author"));
        Author author2 = authorRepo.save(new Author("Author2"));

        repo.save(new Book("Book1", author));
        repo.save(new Book("Book2", author2));
        System.out.println("BOOK COUNT: " + repo.count());

        mockMvc.perform(get("/v2/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[1].title").value("Book2"));
    }

    @Test
    public void shouldGetBookById() throws Exception {

        Author author = new Author("Author");
        Book book = repo.save(new Book("HitchIt", authorRepo.save(author)));

        mockMvc.perform(get("/v2/api/books/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("HitchIt"));
    }

    @Test
    public void shouldUpdateBook() throws Exception {

        Author author = new Author("Author");

        Book book = repo.save(new Book("OldTitle", authorRepo.save(author)));

        UpdateBookRequestDTO updateDTO = UpdateBookRequestDTO.builder()
                .title("UpdatedTitle")
                .build();

        mockMvc.perform(patch("/v2/api/books/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("UpdatedTitle"));

        assertEquals(
                "UpdatedTitle",
                repo.findById(book.getId()).orElseThrow().getTitle()
        );
    }

    @Test
    public void shouldDeleteBook() throws Exception {
        Author author = new Author("Author");
        Book book = repo.save(new Book("ToDelete", authorRepo.save(author)));
        mockMvc.perform(delete("/v2/api/books/" + book.getId()))
                .andExpect(status().isNoContent());

        assertFalse(repo.findById(book.getId()).isPresent());
    }
}