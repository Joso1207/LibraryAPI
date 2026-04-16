package ChasAcademy.LibraryAPI.api.core.dto;

import jakarta.validation.Valid;
import lombok.Builder;
import org.hibernate.validator.constraints.ISBN;

@Builder
public record UpdateBookRequestDTO(
        String title,
        @Valid AuthorReferenceDTO author,
        @ISBN String isbn,
        Integer yearPublished
) {}
