package kz.almaty.moneytransferservice.dto;

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
public class AccountDto {
    private String firstName;
    private String lastName;
    private String accountNumber;
    private BigDecimal accountBalance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
