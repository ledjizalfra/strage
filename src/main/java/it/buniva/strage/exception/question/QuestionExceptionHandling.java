package it.buniva.strage.exception.question;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.exception.answer.AnswerNotFoundException;
import it.buniva.strage.exception.answer.DuplicateAnswerCodeException;
import it.buniva.strage.exception.answer.EmptyAnswerListException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static it.buniva.strage.exception.ExceptionHandling.createHttpResponse;

@RestControllerAdvice
public class QuestionExceptionHandling {

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<ApiResponseCustom> questionNotFoundException(QuestionNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DuplicateQuestionCodeException.class)
    public ResponseEntity<ApiResponseCustom> duplicateQuestionCodeException(DuplicateQuestionCodeException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DuplicateQuestionContentException.class)
    public ResponseEntity<ApiResponseCustom> duplicateQuestionContentException(DuplicateQuestionContentException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmptyQuestionListException.class)
    public ResponseEntity<ApiResponseCustom> emptyQuestionListException(EmptyQuestionListException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
