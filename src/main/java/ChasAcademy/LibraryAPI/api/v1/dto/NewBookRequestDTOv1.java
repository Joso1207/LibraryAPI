package ChasAcademy.LibraryAPI.api.v1.dto;


import ChasAcademy.LibraryAPI.api.v1.validation.ValidAuthorReference;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.ISBN;

@ValidAuthorReference
@Builder //Validation
public record NewBookRequestDTOv1(@NotBlank String title, String author, Long authorID, @ISBN String isbn, Integer yearPublished) {
}
