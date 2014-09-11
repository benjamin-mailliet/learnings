package learnings.exceptions;

public class LearningAccessException extends Exception {

	private static final long serialVersionUID = 7348054554643258527L;

	public LearningAccessException() {
		super();
	}

	public LearningAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LearningAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public LearningAccessException(String message) {
		super(message);
	}

	public LearningAccessException(Throwable cause) {
		super(cause);
	}

}
