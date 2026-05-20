package ChasAcademy.LibraryAPI.api.core.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Schema(description = "Standard error response")
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String,String> details
        ) { }
