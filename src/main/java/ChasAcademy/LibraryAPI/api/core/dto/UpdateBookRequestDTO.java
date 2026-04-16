package ChasAcademy.LibraryAPI.api.core.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import org.hibernate.validator.constraints.ISBN;

import java.time.Year;

@Builder
public record UpdateBookRequestDTO(
        String title,
        @Valid AuthorReferenceDTO author,
        @ISBN String isbn,
        @PastOrPresent Year yearPublished
) {}
