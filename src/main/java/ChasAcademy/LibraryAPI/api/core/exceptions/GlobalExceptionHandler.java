package ChasAcademy.LibraryAPI.api.core.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {


    // Example: handle "book not found"
    //!! NOTE;  IN PRODUCTION WE DO NOT EXPOSE ex.getMessage()
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Map<String, Object>> bookNotFound(BookNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Book Not Found");
        body.put("message", ex.getMessage());
        body.put("Test","This was Generated with a custom Exception");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<Map<String, Object>> authorNotFound(AuthorNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Author Not Found");
        body.put("message", Optional.ofNullable(ex.getMessage())
                .orElse("Author Not Found"));
        body.put("Test","This was Generated with a custom Exception");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<Map<String, Object>> loanNotFound(LoanNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Loan Not Found");
        body.put("message", Optional.ofNullable(ex.getMessage())
                .orElse("Loan not found"));
        body.put("Test","This was Generated with a custom Exception");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<Map<String, Object>> unavailableBook(BookNotAvailableException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Unable to create Loan");
        body.put("message", Optional.ofNullable(ex.getMessage())
                .orElse("Book not available"));
        body.put("Test","This was Generated with a custom Exception");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(DataIntegrityViolationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Bad Request");
        body.put("message", Optional.ofNullable(ex.getMessage())
                .orElse("Database constraint violated"));
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();

        // 1. Field-level errors (Jakarta annotations, @NotNull etc)
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        // 2. Global / class-level ( custom validator)
        ex.getBindingResult().getGlobalErrors().forEach(error ->
                fieldErrors.put("global", error.getDefaultMessage())
        );

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Error");
        body.put("message", "Request validation failed");
        body.put("fields", fieldErrors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleMissingBody(HttpMessageNotReadableException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", "Request body is required");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Generic exception handler for other errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", Optional.ofNullable(ex.getMessage())
                .orElse("An unexpected error occurred"));
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }




}
