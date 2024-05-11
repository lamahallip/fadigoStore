package fr.fadigoStore.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCode {

    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code"),
    CURRENT_PASSWORD_INCORRECT(301, HttpStatus.BAD_REQUEST, "The current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(302, HttpStatus.BAD_REQUEST, "The new password is incorrect"),
    ACCOUNT_LOCKED(303, HttpStatus.FORBIDDEN,"This account is locked"),
    ACCOUNT_DISABLE(304, HttpStatus.FORBIDDEN, "This account is disable"),
    BAD_CREDENTIALS(305, HttpStatus.FORBIDDEN, "Email or password is incorrect");

    private final int code;
    private final HttpStatus httpStatus;
    private final String description;

    BusinessErrorCode(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}
