package ChasAcademy.LibraryAPI.api.v1.mapper;

import ChasAcademy.LibraryAPI.api.core.dto.AuthorDTO;
import ChasAcademy.LibraryAPI.api.core.dto.NewAuthorDTO;
import ChasAcademy.LibraryAPI.persistence.model.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapperV1 {

    public AuthorDTO authorToDTO(Author author){
        return AuthorDTO.builder()
                .name(author.getName())
                .id(author.getId())
                .writtenWorksAmount(author.getWrittenWorks().size())
                .build();
    }

    public NewAuthorDTO dtoToAuthor(AuthorDTO dto){
        return NewAuthorDTO.builder()
                .name(dto.name())
                .build();
    }

}
