package ChasAcademy.LibraryAPI.api.core.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRequest(
        @NotNull String username,

        @NotNull @Size(min = 8,max = 50,message = "Password between 8 and 50 characters")
        String password
) {}
