package it.buniva.strage.exception.admin;

import it.buniva.strage.api.ApiResponseCustom;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static it.buniva.strage.exception.ExceptionHandling.createHttpResponse;

@RestControllerAdvice
public class AdminExceptionHandling {

    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<ApiResponseCustom> adminNotFoundException(AdminNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmptyAdminListException.class)
    public ResponseEntity<ApiResponseCustom> emptyAdminListException(EmptyAdminListException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
