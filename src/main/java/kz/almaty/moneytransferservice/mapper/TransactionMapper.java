package kz.almaty.moneytransferservice.mapper;

import kz.almaty.moneytransferservice.dto.TransactionDto;
import kz.almaty.moneytransferservice.model.Transaction;

public class TransactionMapper {
    public static TransactionDto mapToDto(Transaction transaction) {
        return TransactionDto.builder()
                .transactionType(transaction.getTransactionType())
                .transactionAmount(transaction.getTransactionAmount())
                .accountNumber(transaction.getAccountNumber())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

    public static Transaction mapToEntity(TransactionDto transactionDto) {
        return Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .transactionAmount(transactionDto.getTransactionAmount())
                .accountNumber(transactionDto.getAccountNumber())
                .status(transactionDto.getStatus())
                .createdAt(transactionDto.getCreatedAt().withNano(0))
                .build();
    }
}
