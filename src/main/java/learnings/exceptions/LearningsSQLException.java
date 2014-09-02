package learnings.exceptions;

public class LearningsSQLException extends RuntimeException {

	private static final long serialVersionUID = 8520858908301006744L;

	public LearningsSQLException() {
		super();
	}

	public LearningsSQLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LearningsSQLException(String message, Throwable cause) {
		super(message, cause);
	}

	public LearningsSQLException(String message) {
		super(message);
	}

	public LearningsSQLException(Throwable cause) {
		super(cause);
	}

}
