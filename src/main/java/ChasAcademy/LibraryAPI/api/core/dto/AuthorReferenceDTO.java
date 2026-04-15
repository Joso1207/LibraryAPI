package ChasAcademy.LibraryAPI.api.core.dto;

import ChasAcademy.LibraryAPI.api.core.validation.ValidAuthorReference;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@ValidAuthorReference
@Builder
public record AuthorReferenceDTO(String name,Long id) { }
