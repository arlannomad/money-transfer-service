package kz.almaty.moneytransferservice.exception.already_exists_exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class ResourceAlreadyExistsExceptionData {
    private final String message;
    private final HttpStatus httpStatus;
    private final LocalDateTime localDateTime;
}
