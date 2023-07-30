package kz.almaty.moneytransferservice.exception.not_found_exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class ResourceNotFoundExceptionData {
    private final String message;
    private final HttpStatus httpStatus;
    private final LocalDateTime localDateTime;
}
