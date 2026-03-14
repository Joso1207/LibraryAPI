package ChasAcademy.LibraryAPI.api.mapper;


import ChasAcademy.LibraryAPI.api.dto.BookRequestDTO;
import ChasAcademy.LibraryAPI.api.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public BookRequestDTO toBookDTO(Book book){
        return BookRequestDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn((book.getIsbn()))
                .author(book.getAuthor())
                .yearPublished(book.getPublishedYear())
                .build();
    }

    public Book dtoToBook(NewBookRequestDTO dto){
        return new Book(
                dto.getTitle(),
                dto.getAuthor(),
                dto.getIsbn(),
                dto.getYearPublished());
    }

}
