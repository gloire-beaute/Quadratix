package quadratix;

public class BitLengthExceededException extends RuntimeException {
	
	public BitLengthExceededException(int maximumLength, int actualLength) {
		super("Maximum length is \"" + maximumLength + "\", but actual length is \"" + actualLength + "\".");
	}
}
