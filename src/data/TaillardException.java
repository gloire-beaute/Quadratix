package data;

public class TaillardException extends RuntimeException {

    public TaillardException(String message) {
        super(message);
    }
    public TaillardException(String message, Throwable cause) {
        super(message, cause);
    }
    public TaillardException() {
        super("Wrong Taillard file format");
    }
}
