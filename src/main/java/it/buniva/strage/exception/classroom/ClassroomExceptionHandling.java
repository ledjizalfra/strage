package it.buniva.strage.exception.classroom;

import it.buniva.strage.api.ApiResponseCustom;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static it.buniva.strage.exception.ExceptionHandling.createHttpResponse;

@RestControllerAdvice
public class ClassroomExceptionHandling {

    @ExceptionHandler(DuplicateClassroomNameException.class)
    public ResponseEntity<ApiResponseCustom> duplicateClassroomNameException(DuplicateClassroomNameException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ClassroomNotFoundException.class)
    public ResponseEntity<ApiResponseCustom> classroomNotFoundException(ClassroomNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmptyClassroomListException.class)
    public ResponseEntity<ApiResponseCustom> emptyClassroomListException(EmptyClassroomListException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

}
