package it.buniva.strage.exception.subject;

import it.buniva.strage.api.ApiResponseCustom;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static it.buniva.strage.exception.ExceptionHandling.createHttpResponse;

@RestControllerAdvice
public class SubjectExceptionHandling {

    @ExceptionHandler(SubjectNotFoundException.class)
    public ResponseEntity<ApiResponseCustom> subjectNotFoundException(SubjectNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DuplicateSubjectCodeException.class)
    public ResponseEntity<ApiResponseCustom> duplicateSubjectCodeException(DuplicateSubjectCodeException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmptySubjectListException.class)
    public ResponseEntity<ApiResponseCustom> emptySubjectListException(EmptySubjectListException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(SubjectAlreadyExistsException.class)
    public ResponseEntity<ApiResponseCustom> subjectAlreadyExistsException(SubjectAlreadyExistsException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
