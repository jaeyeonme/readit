package me.jaeyeon.common.exception;

public class MemberNotFoundException extends BlogApiException {

	public MemberNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
