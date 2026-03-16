package ChasAcademy.LibraryAPI.api.core.mapper;

import ChasAcademy.LibraryAPI.api.core.dto.AuthorDTO;
import ChasAcademy.LibraryAPI.api.core.dto.NewAuthorDTO;
import ChasAcademy.LibraryAPI.persistence.model.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    public AuthorDTO authorToDTO(Author author){
        return AuthorDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .build();
    }

    public Author dtotoAuthor(NewAuthorDTO requestDTO){
        return  Author.builder()
                .name(requestDTO.name())
                .build();
    }
}
