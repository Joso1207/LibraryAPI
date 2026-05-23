package ChasAcademy.LibraryAPI.api.core.exceptions;

public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(String message) {
        super(message);
    }
    public RateLimitExceededException(){
        super("Request rate exceeded, try again later");
    }
}
