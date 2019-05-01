package quadratix.data;

public class AssignementDataException extends RuntimeException {

    public AssignementDataException(String message) {
        super(message);
    }
    public AssignementDataException(String message, Throwable cause) {
        super(message, cause);
    }
    public AssignementDataException() {
        super("Wrong assignement data format");
    }
}
