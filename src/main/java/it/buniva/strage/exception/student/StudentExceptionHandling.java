package it.buniva.strage.exception.student;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.exception.csvfile.CSVFileExceptionHandling;
import it.buniva.strage.exception.role.RoleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static it.buniva.strage.exception.ExceptionHandling.createHttpResponse;

@RestControllerAdvice
public class StudentExceptionHandling extends CSVFileExceptionHandling {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ApiResponseCustom> studentNotFoundException(StudentNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmptyStudentListException.class)
    public ResponseEntity<ApiResponseCustom> emptyStudentListException(EmptyStudentListException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DuplicatePersonalDataException.class)
    public ResponseEntity<ApiResponseCustom> duplicatePersonalDataException(DuplicatePersonalDataException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
