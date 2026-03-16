package ChasAcademy.LibraryAPI.api.v1.dto;

import lombok.Builder;


@Builder
public record BookRequestDTOv1(Long id, String title, String author, Long authorID, String isbn, Integer yearPublished) {
}
