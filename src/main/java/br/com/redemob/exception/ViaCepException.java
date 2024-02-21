package br.com.redemob.exception;

public class ViaCepException extends Exception {

	private static final long serialVersionUID = -8089438826383252393L;

	public ViaCepException(String message) {
        super(message);
    }

    public ViaCepException(String message, Throwable cause) {
        super(message, cause);
    }
}
