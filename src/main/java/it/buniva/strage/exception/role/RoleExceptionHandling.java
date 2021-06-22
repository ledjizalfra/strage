package it.buniva.strage.exception.role;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.exception.professor.EmptyProfessorListException;
import it.buniva.strage.exception.professor.ProfessorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static it.buniva.strage.exception.ExceptionHandling.createHttpResponse;

@RestControllerAdvice
public class RoleExceptionHandling {

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiResponseCustom> roleNotFoundException(RoleNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
