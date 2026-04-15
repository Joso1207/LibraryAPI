package ChasAcademy.LibraryAPI.api.core.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record NewAuthorDTO(@NotBlank String name) { }
