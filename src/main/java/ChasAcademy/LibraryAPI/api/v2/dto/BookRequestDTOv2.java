package ChasAcademy.LibraryAPI.api.v2.dto;

import ChasAcademy.LibraryAPI.api.core.dto.AuthorDTO;
import lombok.Builder;


@Builder
public record BookRequestDTOv2(Long id, String title, AuthorDTO author, String isbn, Integer yearPublished) {
}
