package kz.almaty.moneytransferservice.service.impl;

import kz.almaty.moneytransferservice.dto.TransactionDto;
import kz.almaty.moneytransferservice.enums.Status;
import kz.almaty.moneytransferservice.mapper.TransactionMapper;
import kz.almaty.moneytransferservice.model.Transaction;
import kz.almaty.moneytransferservice.repository.TransactionRepository;
import kz.almaty.moneytransferservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .transactionAmount(transactionDto.getTransactionAmount())
                .accountNumber(transactionDto.getAccountNumber())
                .status(Status.SUCCESS)
                .build();
        transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully");
    }

    @Override
    public List<TransactionDto> getAll() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream().map(TransactionMapper::mapToDto).toList();
    }

}
