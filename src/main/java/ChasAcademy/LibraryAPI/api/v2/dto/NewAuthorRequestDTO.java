package ChasAcademy.LibraryAPI.api.v2.dto;

import lombok.Builder;

@Builder
public record NewAuthorRequestDTO(String name) {
}
