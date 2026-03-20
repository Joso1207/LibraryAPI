package ChasAcademy.LibraryAPI.api.v1.dto;


import lombok.Builder;

@Builder
public record NewBookRequestDTOv1(String title, String author,Long authorID, String isbn, Integer yearPublished) {
}
