package it.buniva.strage.exception.argument;

import it.buniva.strage.api.ApiResponseCustom;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static it.buniva.strage.exception.ExceptionHandling.createHttpResponse;

@RestControllerAdvice
public class ArgumentExceptionHandling {

    @ExceptionHandler(ArgumentNotFoundException.class)
    public ResponseEntity<ApiResponseCustom> argumentNotFoundException(ArgumentNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DuplicateArgumentCodeException.class)
    public ResponseEntity<ApiResponseCustom> duplicateArgumentCodeException(DuplicateArgumentCodeException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmptyArgumentListException.class)
    public ResponseEntity<ApiResponseCustom> emptyArgumentListException(EmptyArgumentListException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ArgumentNotInSameSubjectException.class)
    public ResponseEntity<ApiResponseCustom> argumentNotInSameSubjectException(ArgumentNotInSameSubjectException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
