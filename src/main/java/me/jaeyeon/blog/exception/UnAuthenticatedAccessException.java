package me.jaeyeon.blog.exception;

public class UnAuthenticatedAccessException extends BlogApiException {

	public UnAuthenticatedAccessException(ErrorCode errorCode) {
		super(errorCode);
	}
}
