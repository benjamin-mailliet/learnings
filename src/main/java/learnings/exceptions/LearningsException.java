package learnings.exceptions;

public class LearningsException extends Exception {

	private static final long serialVersionUID = 8520858908301006744L;

	public LearningsException() {
		super();
	}

	public LearningsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LearningsException(String message, Throwable cause) {
		super(message, cause);
	}

	public LearningsException(String message) {
		super(message);
	}

	public LearningsException(Throwable cause) {
		super(cause);
	}

}
