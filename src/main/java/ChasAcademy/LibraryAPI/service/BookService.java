package ChasAcademy.LibraryAPI.service;

import ChasAcademy.LibraryAPI.api.dto.BookRequestDTO;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.persistence.repository.BookRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repo;

    @Autowired
    public BookService(BookRepository repo){
        this.repo = repo;
    }

    public @NonNull BookRequestDTO getBookByID(Long id){
       Book entity = repo.findById(id).orElseThrow();

       return BookRequestDTO.builder()
               .id(entity.getId())
               .title(entity.getTitle())
               .isbn((entity.getIsbn()))
               .author(entity.getAuthor())
               .yearPublished(entity.getPublishedYear())
               .build();
    }


}
