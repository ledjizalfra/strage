package it.buniva.strage.exception.configuration;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.exception.classroom.ClassroomNotFoundException;
import it.buniva.strage.exception.classroom.DuplicateClassroomNameException;
import it.buniva.strage.exception.classroom.EmptyClassroomListException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static it.buniva.strage.exception.ExceptionHandling.createHttpResponse;

@RestControllerAdvice
public class ConfigurationExceptionHandling {

    @ExceptionHandler(DuplicateConfigurationNameException.class)
    public ResponseEntity<ApiResponseCustom> duplicateConfigurationNameException(DuplicateConfigurationNameException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ConfigurationNotFoundException.class)
    public ResponseEntity<ApiResponseCustom> configurationNotFoundException(ConfigurationNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ConfigurationCreationFailedException.class)
    public ResponseEntity<ApiResponseCustom> configurationCreationFailedException(ConfigurationCreationFailedException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }


}
