package ChasAcademy.LibraryAPI.api.v1.dto;


import ChasAcademy.LibraryAPI.api.v1.validation.ValidAuthorReferencev1;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import org.hibernate.validator.constraints.ISBN;

import java.time.Year;

@ValidAuthorReferencev1
@Builder //Validation
public record NewBookRequestDTOv1(@NotBlank String title, String author, Long authorID, @ISBN String isbn,@PastOrPresent Year yearPublished) {
}
