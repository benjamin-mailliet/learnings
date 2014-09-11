package learnings.exceptions;

public class LearningsSecuriteException extends Exception {

	private static final long serialVersionUID = 8520858908301006744L;

	public LearningsSecuriteException() {
		super();
	}

	public LearningsSecuriteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LearningsSecuriteException(String message, Throwable cause) {
		super(message, cause);
	}

	public LearningsSecuriteException(String message) {
		super(message);
	}

	public LearningsSecuriteException(Throwable cause) {
		super(cause);
	}

}
