package kz.almaty.moneytransferservice.exception.insufficient_balance;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class InsufficientBalanceExceptionHandler {
    @ExceptionHandler(value = {InsufficientBalanceException.class})
    ResponseEntity<Object> handleException(InsufficientBalanceException exception) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        InsufficientBalanceExceptionData exceptionData = new InsufficientBalanceExceptionData(
                exception.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now());
        return new ResponseEntity<>(exceptionData, notFound);
    }
}
