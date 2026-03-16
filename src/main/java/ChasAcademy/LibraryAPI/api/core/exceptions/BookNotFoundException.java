package ChasAcademy.LibraryAPI.api.core.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) {
        super("Book with ID " + id + " not found");
    }
}
