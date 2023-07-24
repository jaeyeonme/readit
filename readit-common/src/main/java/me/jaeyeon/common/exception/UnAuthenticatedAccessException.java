package me.jaeyeon.common.exception;

public class UnAuthenticatedAccessException extends BlogApiException {

	public UnAuthenticatedAccessException(ErrorCode errorCode) {
		super(errorCode);
	}
}
