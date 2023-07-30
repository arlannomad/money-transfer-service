package kz.almaty.moneytransferservice.exception.insufficient_balance;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
