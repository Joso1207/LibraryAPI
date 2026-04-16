package ChasAcademy.LibraryAPI.api.core.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.hibernate.validator.constraints.ISBN;

import java.time.Year;

@Builder
public record NewBookRequestDTO(@NotBlank String title, @Valid @NotNull AuthorReferenceDTO author, @ISBN String isbn, @PastOrPresent Year yearPublished) {
}
