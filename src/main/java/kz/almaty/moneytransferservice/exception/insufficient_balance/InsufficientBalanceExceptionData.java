package kz.almaty.moneytransferservice.exception.insufficient_balance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class InsufficientBalanceExceptionData {
    private final String message;
    private final HttpStatus httpStatus;
    private final LocalDateTime localDateTime;
}
