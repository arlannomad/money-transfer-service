package kz.almaty.moneytransferservice.controller;

import kz.almaty.moneytransferservice.dto.TransactionDto;
import kz.almaty.moneytransferservice.service.impl.TransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionServiceImpl transactionService;

    @GetMapping
    public List<TransactionDto> getAllTransactions() {
        return transactionService.getAll();
    }
}
