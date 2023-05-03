package me.jaeyeon.blog.exception;

import lombok.Getter;

@Getter
public class BlogApiException extends RuntimeException {

	private ErrorCode errorCode;

	public BlogApiException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
