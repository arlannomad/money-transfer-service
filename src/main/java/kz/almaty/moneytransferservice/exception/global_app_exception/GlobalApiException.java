package kz.almaty.moneytransferservice.exception.global_app_exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class GlobalApiException extends RuntimeException{
    private HttpStatus httpStatus;
    private String message;


    public GlobalApiException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }
}