package ChasAcademy.LibraryAPI.api.core.dto;

import ChasAcademy.LibraryAPI.persistence.model.Author;
import lombok.*;


@Builder
public record BookRequestDTO(Long id, String title, Author author, String isbn, Integer yearPublished) {
}
