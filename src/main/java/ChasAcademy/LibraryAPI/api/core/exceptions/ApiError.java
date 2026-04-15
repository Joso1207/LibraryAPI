package ChasAcademy.LibraryAPI.api.core.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Standard error response")
public class ApiError {

    public LocalDateTime timestamp;
    public int status;
    public String error;
    public String message;
    public Map<String, String> fields;
}
