package quadratix.assignement;

public class AssignmentDataException extends RuntimeException {

    public AssignmentDataException(String message) {
        super(message);
    }
    public AssignmentDataException(String message, Throwable cause) {
        super(message, cause);
    }
    public AssignmentDataException() {
        super("Wrong assignement data format");
    }
}
