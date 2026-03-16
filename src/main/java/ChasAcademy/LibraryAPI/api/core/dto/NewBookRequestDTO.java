package ChasAcademy.LibraryAPI.api.core.dto;


import lombok.*;

@Builder
public record NewBookRequestDTO(String title, String author, String isbn, Integer yearPublished) {
}
