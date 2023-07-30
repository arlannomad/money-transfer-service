package kz.almaty.moneytransferservice.dto;

import kz.almaty.moneytransferservice.enums.Status;
import kz.almaty.moneytransferservice.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
    private TransactionType transactionType;
    private BigDecimal transactionAmount;
    private String accountNumber;
    private Status status;
    private LocalDateTime createdAt;
}
