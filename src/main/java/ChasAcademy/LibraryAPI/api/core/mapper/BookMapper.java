package ChasAcademy.LibraryAPI.api.core.mapper;

import ChasAcademy.LibraryAPI.api.core.dto.AuthorDTO;
import ChasAcademy.LibraryAPI.api.core.dto.BookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.persistence.model.Author;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookRequestDTO toBookDTO(Book book){
        return BookRequestDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .yearPublished(book.getPublishedYear())
                .author(AuthorDTO.builder()
                        .id(book.getAuthor().getId())
                        .name(book.getAuthor().getName())
                        .build())
                .build();
    }

    public Book dtoToBook(NewBookRequestDTO dto){
        return  Book.builder()
                .title(dto.title())
                .isbn(dto.isbn())
                .publishedYear(dto.yearPublished())
                .author(Author.builder()
                        .id(dto.author().id())
                        .name(dto.author().name())
                        .build()
                )
                .build();
    }

}
