package it.buniva.strage.exception.professor;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.exception.admin.AdminNotFoundException;
import it.buniva.strage.exception.admin.EmptyAdminListException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static it.buniva.strage.exception.ExceptionHandling.createHttpResponse;

@RestControllerAdvice
public class ProfessorExceptionHandling {

    @ExceptionHandler(ProfessorNotFoundException.class)
    public ResponseEntity<ApiResponseCustom> professorNotFoundException(ProfessorNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmptyProfessorListException.class)
    public ResponseEntity<ApiResponseCustom> emptyProfessorListException(EmptyProfessorListException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
