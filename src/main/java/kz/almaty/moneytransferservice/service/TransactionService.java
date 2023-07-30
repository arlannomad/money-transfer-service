package kz.almaty.moneytransferservice.service;

import kz.almaty.moneytransferservice.dto.TransactionDto;

import java.util.List;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
    List<TransactionDto> getAll();
}
