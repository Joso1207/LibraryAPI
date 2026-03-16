package ChasAcademy.LibraryAPI.api.v1.mapper;


import ChasAcademy.LibraryAPI.api.core.dto.BookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.dto.NewBookRequestDTO;
import ChasAcademy.LibraryAPI.api.core.mapper.BookMapper;
import ChasAcademy.LibraryAPI.api.v1.dto.BookRequestDTOv1;
import ChasAcademy.LibraryAPI.api.v1.dto.NewBookRequestDTOv1;
import org.springframework.stereotype.Component;

@Component
public class BookMapperV1 {

    public BookMapper coreMapper;

    public BookMapperV1 (BookMapper mapper){
        this.coreMapper = mapper;
    }

    public BookRequestDTOv1 coreToV1DTO(BookRequestDTO internal){
        return BookRequestDTOv1.builder()
                .id(internal.id())
                .title(internal.title())
                .authorID(internal.author().id())
                .author(internal.author().name())
                .isbn(internal.isbn())
                .yearPublished(internal.yearPublished())
                .build();
    }

    public NewBookRequestDTO v1ToCoreDTO(NewBookRequestDTOv1 dto){
        return NewBookRequestDTO.builder()
                .title(dto.title())
                .isbn(dto.isbn())
                .yearPublished(dto.yearPublished())
                .build();
    }

}
