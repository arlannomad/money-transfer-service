package kz.almaty.moneytransferservice.mapper;

import kz.almaty.moneytransferservice.dto.AccountDto;
import kz.almaty.moneytransferservice.model.Account;

import java.math.BigDecimal;

public class AccountMapper {
    public static AccountDto mapToDto(Account account) {
        return AccountDto.builder()
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .accountNumber(account.getAccountNumber())
                .accountBalance(account.getAccountBalance())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

    public static Account mapToAccount(AccountDto accountDto) {
        return Account.builder()
                .firstName(accountDto.getFirstName())
                .lastName(accountDto.getLastName())
                .accountNumber(accountDto.getAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .createdAt(accountDto.getCreatedAt())
                .updatedAt(accountDto.getUpdatedAt())
                .build();
    }
}
