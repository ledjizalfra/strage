package it.buniva.strage.exception.user;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.exception.ExceptionHandling;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class UserExceptionHandling extends ExceptionHandling {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponseCustom> userNotFoundException(UserNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmptyUserListException.class)
    public ResponseEntity<ApiResponseCustom> emptyUserListException(EmptyUserListException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ApiResponseCustom> duplicateUsernameException(DuplicateUsernameException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(PasswordNotMatchesException.class)
    public ResponseEntity<ApiResponseCustom> passwordNoMatchesException(PasswordNotMatchesException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
    @ExceptionHandler(InvalidPasswordFormatException.class)
    public ResponseEntity<ApiResponseCustom> invalidPasswordFormatException(InvalidPasswordFormatException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
