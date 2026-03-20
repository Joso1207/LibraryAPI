package ChasAcademy.LibraryAPI.api.v2.mapper;

import ChasAcademy.LibraryAPI.api.core.dto.AuthorDTO;
import ChasAcademy.LibraryAPI.api.v2.dto.BookRequestDTOv2;
import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.v2.dto.NewBookRequestDTOv2;
import ChasAcademy.LibraryAPI.persistence.model.Book;

public class BookMapperV2 {

    public BookRequestDTOv2 toBookDTO(Book book){
        return BookRequestDTOv2.builder()
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

    public NewBookRequestDTO dtoToBook(NewBookRequestDTOv2 dto){
        return  NewBookRequestDTO.builder()
                .title(dto.title())
                .isbn(dto.isbn())
                .yearPublished(dto.yearPublished())
                .author(AuthorDTO.builder()
                        .id(dto.author().id())
                        .name(dto.author().name())
                        .build()
                )
                .build();
    }

}

