package ChasAcademy.LibraryAPI.api.v1.mapper;


import ChasAcademy.LibraryAPI.api.core.dto.AuthorReferenceDTO;
import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.v1.dto.BookResponseDTOv1;
import ChasAcademy.LibraryAPI.api.v1.dto.NewBookRequestDTOv1;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.service.viewModels.BookViewModel;
import org.springframework.stereotype.Component;

@Component
public class BookMapperV1 {
    

    public BookResponseDTOv1 bookToDTOV1(Book book){
        return BookResponseDTOv1.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorID(book.getAuthor().getId())
                .author(book.getAuthor().getName())
                .isbn(book.getIsbn())
                .yearPublished(book.getPublishedYear())
                .build();
    }

    public BookResponseDTOv1 bookToDTOV1(BookViewModel book){
        return BookResponseDTOv1.builder()
                .id(book.id())
                .title(book.title())
                .authorID(book.authorID())
                .author(book.author())
                .isbn(book.isbn())
                .yearPublished(book.yearPublished())
                .build();
    }

    public NewBookRequestDTO v1dtoToBookRequest(NewBookRequestDTOv1 dto){
        return NewBookRequestDTO.builder()
                .title(dto.title())
                .isbn(dto.isbn())
                .yearPublished(dto.yearPublished())
                .author(AuthorReferenceDTO.builder()
                        .id(dto.authorID())
                        .name(dto.author())
                        .build()
                )
                .build();
    }

}
