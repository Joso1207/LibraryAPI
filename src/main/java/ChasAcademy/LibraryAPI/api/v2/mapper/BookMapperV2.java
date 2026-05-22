package ChasAcademy.LibraryAPI.api.v2.mapper;


import ChasAcademy.LibraryAPI.api.core.dto.AuthorDTO;
import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.v1.dto.BookResponseDTOv1;
import ChasAcademy.LibraryAPI.api.v2.dto.BookResponseDTOv2;
import ChasAcademy.LibraryAPI.persistence.model.Author;
import ChasAcademy.LibraryAPI.persistence.model.Book;
import ChasAcademy.LibraryAPI.service.viewModels.BookViewModel;
import org.springframework.stereotype.Component;

@Component
public class BookMapperV2 {

    private final AuthorMapperV2 authorMapper;

    public BookMapperV2(AuthorMapperV2 authorMapper) {
        this.authorMapper = authorMapper;
    }

    public BookResponseDTOv2 bookToDTOV2(Book book, Boolean available){
        return BookResponseDTOv2.builder()
                .id(book.getId())
                .available(available)
                .title(book.getTitle())
                .author(authorMapper.authorToDTO(book.getAuthor()))
                .isbn(book.getIsbn())
                .yearPublished(book.getPublishedYear())
                .build();
    }

    public BookResponseDTOv2 bookToDTOV2(BookViewModel book, Boolean available){
        return BookResponseDTOv2.builder()
                .id(book.id())
                .available(available)
                .title(book.title())
                .author(new AuthorDTO(book.author(), book.authorID(), book.authorBookAmount()))
                .isbn(book.isbn())
                .yearPublished(book.yearPublished())
                .build();
    }

    public NewBookRequestDTO v2dtoToBookRequest(NewBookRequestDTO dto){
        return NewBookRequestDTO.builder()
                .title(dto.title())
                .isbn(dto.isbn())
                .yearPublished(dto.yearPublished())
                .author(dto.author())
                .build();
    }

}
