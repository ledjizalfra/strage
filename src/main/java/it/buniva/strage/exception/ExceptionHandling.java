package it.buniva.strage.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import it.buniva.strage.api.ApiErrorsView;
import it.buniva.strage.api.ApiFieldError;
import it.buniva.strage.api.ApiGlobalError;
import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.constant.ExceptionHandlerConstant;
import it.buniva.strage.exception.csvfile.FileNotPresentException;
import it.buniva.strage.exception.pwdresettoken.InvalidPwdResetTokenException;
import it.buniva.strage.exception.pwdresettoken.PwdResetTokenExpiredException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandling {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public static ResponseEntity<ApiResponseCustom> createHttpResponse(
            HttpStatus httpStatus,
            String message) {

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), httpStatus.value(),
                httpStatus, "", message, ""),
                httpStatus);
    }


    // Method argument not valid Exception
    public static ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex){

        BindingResult bindingResult = ex.getBindingResult();

        List<ApiFieldError> apiFieldErrors = bindingResult
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ApiFieldError(
                        fieldError.getField(),
                        fieldError.getCode(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue())
                ).collect(Collectors.toList());

        List<ApiGlobalError> apiGlobalErrors = bindingResult
                .getGlobalErrors()
                .stream()
                .map(globalError -> new ApiGlobalError(
                        globalError.getCode())
                ).collect(Collectors.toList());

        ApiErrorsView apiErrorsView = new ApiErrorsView(apiFieldErrors, apiGlobalErrors);

        return new ResponseEntity<Object>(new ApiResponseCustom(Instant.now(), 422,
                HttpStatus.UNPROCESSABLE_ENTITY, "Method Argument Not Valid Exception", apiErrorsView,
                StringUtils.EMPTY),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // Constraint violation Exception during write/update on DB
    private ResponseEntity<Object> constraintViolationExceptionHandler(ConstraintViolationException ex) {

        Map<String, Object> result = new HashMap<>();

        List<ApiFieldError> apiFieldErrorList =  ex.getConstraintViolations().stream()
                .map(e -> new ApiFieldError(
                        e.getPropertyPath().toString(), // field
                        e.getPropertyPath().toString(), // Code
                        e.getMessage(),
                        e.getInvalidValue()))
                .collect(Collectors.toList());

        result.put("Field Error", apiFieldErrorList);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 422,
                HttpStatus.UNPROCESSABLE_ENTITY, "Constraint Violation Exception", result,
                StringUtils.EMPTY),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
    // Constraint violation Exception handling
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> methodArgumentNotValidException(ConstraintViolationException exception) {
        return constraintViolationExceptionHandler(exception);
    }

    // Method argument not valid Exception handling
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return handleMethodArgumentNotValid(exception);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponseCustom> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, String.format(ExceptionHandlerConstant.METHOD_IS_NOT_ALLOWED, supportedMethod));
    }

    // =================================== PASSWORD RESET TOKEN ========================================
    @ExceptionHandler(InvalidPwdResetTokenException.class)
    public ResponseEntity<ApiResponseCustom> invalidPwdResetTokenException(InvalidPwdResetTokenException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(PwdResetTokenExpiredException.class)
    public ResponseEntity<ApiResponseCustom> pwdResetTokenExpiredException(PwdResetTokenExpiredException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    // =================================== ILLEGAL ARGUMENT EXCEPTION =====================================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseCustom> illegalArgumentException(IllegalArgumentException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    // =================================== SPRING SECURITY ========================================
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponseCustom> accountDisabledException() {
        return createHttpResponse(HttpStatus.BAD_REQUEST, ExceptionHandlerConstant.ACCOUNT_DISABLED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseCustom> badCredentialsException() {
        return createHttpResponse(HttpStatus.BAD_REQUEST, ExceptionHandlerConstant.INCORRECT_CREDENTIALS);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiResponseCustom> lockedException() {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, ExceptionHandlerConstant.ACCOUNT_LOCKED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseCustom> accessDeniedException() {
        return createHttpResponse(HttpStatus.FORBIDDEN, ExceptionHandlerConstant.NOT_ENOUGH_PERMISSION);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiResponseCustom> tokenExpiredException(TokenExpiredException exception) {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    // =================================== NOT FOUND EXCEPTION =====================================
    @RequestMapping(ExceptionHandlerConstant.ERROR_PATH)
    public ResponseEntity<ApiResponseCustom> notFound404() {
        return createHttpResponse(HttpStatus.NOT_FOUND, ExceptionHandlerConstant.NOT_FOUND_MSG);
    }

    // =================================== OTHER EXCEPTION =====================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseCustom> internalServerErrorException(Exception exception) {
        LOGGER.error(exception.getMessage());
        LOGGER.error("CLASS EXCEPTION NAME: " + exception.getClass().getName());
        return createHttpResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ExceptionHandlerConstant.INTERNAL_SERVER_ERROR_MSG
                        + " ===> " + exception.getMessage() + " : "
                        + Arrays.toString(exception.getStackTrace())
        );
    }
}
