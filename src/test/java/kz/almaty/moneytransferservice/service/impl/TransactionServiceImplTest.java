package kz.almaty.moneytransferservice.service.impl;

import kz.almaty.moneytransferservice.dto.TransactionDto;
import kz.almaty.moneytransferservice.enums.Status;
import kz.almaty.moneytransferservice.enums.TransactionType;
import kz.almaty.moneytransferservice.model.Transaction;
import kz.almaty.moneytransferservice.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionServiceImpl transactionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionServiceImpl(transactionRepository);
    }

    @Test
    public void testSaveTransaction() {
        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(null);

        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType(TransactionType.TRANSFER)
                .transactionAmount(BigDecimal.valueOf(500))
                .accountNumber("1")
                .build();

        transactionService.saveTransaction(transactionDto);

        Mockito.verify(transactionRepository).save(Mockito.any(Transaction.class));
    }

    @Test
    public void testGetAll() {
        Transaction transaction1 = Transaction.builder()
                .transactionType(TransactionType.TRANSFER)
                .transactionAmount(BigDecimal.valueOf(500))
                .accountNumber("1")
                .status(Status.SUCCESS)
                .createdAt(LocalDateTime.now().withNano(0))
                .build();
        Transaction transaction2 = Transaction.builder()
                .transactionType(TransactionType.CREDIT)
                .transactionAmount(BigDecimal.valueOf(100))
                .accountNumber("2")
                .status(Status.SUCCESS)
                .createdAt(LocalDateTime.now().withNano(0))
                .build();
        List<Transaction> mockTransactions = Arrays.asList(transaction1, transaction2);
        Mockito.when(transactionRepository.findAll()).thenReturn(mockTransactions);

        List<TransactionDto> result = transactionService.getAll();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(BigDecimal.valueOf(500), result.get(0).getTransactionAmount());
        Assertions.assertEquals("1", result.get(0).getAccountNumber());
        Assertions.assertEquals(BigDecimal.valueOf(100), result.get(1).getTransactionAmount());
        Assertions.assertEquals("2", result.get(1).getAccountNumber());
    }
}