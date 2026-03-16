package ChasAcademy.LibraryAPI.api.core.mapper;

import ChasAcademy.LibraryAPI.api.core.BookCreationCMD;
import ChasAcademy.LibraryAPI.api.core.dto.BookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookRequestDTO toBookDTO(Book book){
        return BookRequestDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorID(book.getAuthor().getId())
                .isbn(book.getIsbn())
                .yearPublished(book.getPublishedYear())
                .build();
    }

    public Book dtoToBook(NewBookRequestDTO dto){
        return  Book.builder()
                .title(dto.title())
                //authorDTO
                .isbn(dto.isbn())
                .publishedYear(dto.yearPublished())
                .build();
    }

}
