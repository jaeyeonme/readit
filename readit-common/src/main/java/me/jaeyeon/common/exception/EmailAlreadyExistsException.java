package me.jaeyeon.common.exception;

import lombok.Getter;

@Getter
public class EmailAlreadyExistsException extends BlogApiException {
	public EmailAlreadyExistsException(ErrorCode errorCode) {
		super(errorCode);
	}
}
