package me.jaeyeon.blog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "P-001", "Post not founded"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "C-001", "Comment not founded"),
    COMMENT_NOT_EQUALS_POST(HttpStatus.BAD_REQUEST, "C-002", "This is not a comment on the post."),
    ;

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    private HttpStatus httpStatus;
    private String errorCode;
    private String message;
}
