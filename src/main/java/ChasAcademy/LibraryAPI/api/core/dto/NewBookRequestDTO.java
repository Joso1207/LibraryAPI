package ChasAcademy.LibraryAPI.api.core.dto;


import lombok.*;

@Builder
public record NewBookRequestDTO(String title, AuthorDTO author, String isbn, Integer yearPublished) {
}
