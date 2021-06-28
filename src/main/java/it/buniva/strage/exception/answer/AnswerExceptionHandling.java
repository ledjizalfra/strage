package it.buniva.strage.exception.answer;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.exception.argument.ArgumentNotFoundException;
import it.buniva.strage.exception.argument.DuplicateArgumentCodeException;
import it.buniva.strage.exception.argument.EmptyArgumentListException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static it.buniva.strage.exception.ExceptionHandling.createHttpResponse;

@RestControllerAdvice
public class AnswerExceptionHandling {

    @ExceptionHandler(AnswerNotFoundException.class)
    public ResponseEntity<ApiResponseCustom> answerNotFoundException(AnswerNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DuplicateAnswerCodeException.class)
    public ResponseEntity<ApiResponseCustom> duplicateAnswerCodeException(DuplicateAnswerCodeException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DuplicateAnswerContentException.class)
    public ResponseEntity<ApiResponseCustom> duplicateAnswerContentException(DuplicateAnswerContentException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmptyAnswerListException.class)
    public ResponseEntity<ApiResponseCustom> emptyAnswerListException(EmptyAnswerListException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DefaultAnswerAlreadyExistException.class)
    public ResponseEntity<ApiResponseCustom> defaultAnswerAlreadyExistException(DefaultAnswerAlreadyExistException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NoWantAnswerAlreadyExistException.class)
    public ResponseEntity<ApiResponseCustom> noWantAnswerAlreadyExistException(NoWantAnswerAlreadyExistException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(AnswerTrueAlreadyExistException.class)
    public ResponseEntity<ApiResponseCustom> answerTrueAlreadyExistException(AnswerTrueAlreadyExistException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InvalidAnswerRequestFormException.class)
    public ResponseEntity<ApiResponseCustom> invalidAnswerRequestFormException(InvalidAnswerRequestFormException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
