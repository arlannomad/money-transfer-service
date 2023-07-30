package kz.almaty.moneytransferservice.exception.already_exists_exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ResourceAlreadyExistsExceptionHandler {
    @ExceptionHandler(value = {ResourceAlreadyExistsException.class})
    ResponseEntity<Object> handleException(ResourceAlreadyExistsException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ResourceAlreadyExistsExceptionData alreadyExistsExceptionData = new ResourceAlreadyExistsExceptionData(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
        return new ResponseEntity<>(alreadyExistsExceptionData, badRequest);
    }
}
