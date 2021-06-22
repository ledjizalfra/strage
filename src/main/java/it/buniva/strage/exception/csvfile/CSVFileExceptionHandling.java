package it.buniva.strage.exception.csvfile;

import it.buniva.strage.api.ApiResponseCustom;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static it.buniva.strage.exception.ExceptionHandling.createHttpResponse;

@RestControllerAdvice
public class CSVFileExceptionHandling {

    @ExceptionHandler(FileNotPresentException.class)
    public ResponseEntity<ApiResponseCustom> fileNotPresentException(FileNotPresentException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(CSVEmailFormatException.class)
    public ResponseEntity<ApiResponseCustom> cSVEmailFormatException(CSVEmailFormatException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(CSVHeaderFieldNotFoundException.class)
    public ResponseEntity<ApiResponseCustom> cSVHeaderFieldNotFoundException(CSVHeaderFieldNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(CSVInconsistentRecordException.class)
    public ResponseEntity<ApiResponseCustom> cSVInconsistentRecordException(CSVInconsistentRecordException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(CSVNameFormatException.class)
    public ResponseEntity<ApiResponseCustom> cSVNameFormatException(CSVNameFormatException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(CSVNumberFormatException.class)
    public ResponseEntity<ApiResponseCustom> cSVNumberFormatException(CSVNumberFormatException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InvalidNumberHeaderFieldException.class)
    public ResponseEntity<ApiResponseCustom> invalidNumberHeaderFieldException(InvalidNumberHeaderFieldException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NoItemFoundInFileException.class)
    public ResponseEntity<ApiResponseCustom> noItemFoundInFileException(NoItemFoundInFileException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(TypeFileNotCorrectException.class)
    public ResponseEntity<ApiResponseCustom> typeFileNotCorrectException(TypeFileNotCorrectException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
