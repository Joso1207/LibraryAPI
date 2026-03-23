package ChasAcademy.LibraryAPI.api.core.exceptions;

public class BookNotAvailableException extends RuntimeException{
    public BookNotAvailableException(Long id){
        super("Book with ID " + id + " is not currently available for loan");
    }
}
