package kz.almaty.moneytransferservice.service.impl;

import kz.almaty.moneytransferservice.dto.CreditDebitRequest;
import kz.almaty.moneytransferservice.dto.TransactionDto;
import kz.almaty.moneytransferservice.dto.TransferRequest;
import kz.almaty.moneytransferservice.dto.AccountDto;
import kz.almaty.moneytransferservice.enums.TransactionType;
import kz.almaty.moneytransferservice.exception.already_exists_exception.ResourceAlreadyExistsException;
import kz.almaty.moneytransferservice.exception.insufficient_balance.InsufficientBalanceException;
import kz.almaty.moneytransferservice.exception.not_found_exception.ResourceNotFoundException;
import kz.almaty.moneytransferservice.mapper.AccountMapper;
import kz.almaty.moneytransferservice.model.Transaction;
import kz.almaty.moneytransferservice.model.Account;
import kz.almaty.moneytransferservice.repository.AccountRepository;
import kz.almaty.moneytransferservice.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.testcontainers.shaded.org.awaitility.Awaitility.given;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    TransactionService transactionService;
    private Account account;
    private AccountDto accountDto;

    private Transaction transaction;
    private TransactionDto transactionDto;


    @BeforeEach
    public void setUp() {
        accountDto = AccountDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .accountNumber("1")
                .accountBalance(BigDecimal.valueOf(100))
                .createdAt(LocalDateTime.now().withNano(0))
                .updatedAt(LocalDateTime.now().withNano(0))
                .build();

        account = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .accountNumber("1")
                .accountBalance(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now().withNano(0))
                .updatedAt(LocalDateTime.now().withNano(0))
                .build();
    }

    @Test
    public void testAddAccount() {

        when(accountRepository.existsByAccountNumber(accountDto.getAccountNumber())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountDto savedAccountDto = accountService.addAccount(accountDto);

        assertThat(savedAccountDto).isNotNull();
        assertEquals("firstName", savedAccountDto.getFirstName());
        assertEquals("lastName", savedAccountDto.getLastName());

        verify(accountRepository).existsByAccountNumber(accountDto.getAccountNumber());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    public void testAddAccount_AccountAlreadyExists() {

        when(accountRepository.existsByAccountNumber(accountDto.getAccountNumber())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            accountService.addAccount(accountDto);
        });

    }

    @Test
    public void testFindByAccountNumber_AccountExists() {
        CreditDebitRequest request = new CreditDebitRequest();
        request.setAccountNumber("1");

        when(accountRepository.existsByAccountNumber(request.getAccountNumber())).thenReturn(true);
        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(account);

        AccountDto dto = accountService.findByAccountNumber(request);

        assertEquals("1", dto.getAccountNumber());

        verify(accountRepository).existsByAccountNumber("1");
        verify(accountRepository).findByAccountNumber("1");
    }

    @Test
    public void testBalanceEnquiry_AccountNotExists() {

        CreditDebitRequest request = new CreditDebitRequest();
        when(accountRepository.existsByAccountNumber(null)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.findByAccountNumber(request);
        });
    }

    @Test
    public void testCreditAccount_ExistingAccount() {
        transaction = Transaction.builder()
                .transactionType(TransactionType.CREDIT)
                .transactionAmount(BigDecimal.valueOf(10))
                .accountNumber("1")
                .build();

        transactionDto = TransactionDto.builder()
                .transactionType(TransactionType.CREDIT)
                .transactionAmount(BigDecimal.valueOf(100))
                .accountNumber("1")
                .build();

        CreditDebitRequest request = new CreditDebitRequest();
        request.setAccountNumber("1");
        request.setAmount(BigDecimal.valueOf(100));
        account.setAccountBalance(account.getAccountBalance().add(request.getAmount()));

        when(accountRepository.existsByAccountNumber(request.getAccountNumber())).thenReturn(true);
        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(account);
        doNothing().when(transactionService).saveTransaction(transactionDto);

        accountDto = accountService.creditAccount(request);

        assertEquals("1", accountDto.getAccountNumber());

        verify(accountRepository).existsByAccountNumber("1");
        verify(accountRepository).findByAccountNumber("1");
    }

    @Test
    public void testCreditAccount_NonExistingAccount() {
        CreditDebitRequest request = new CreditDebitRequest();
        when(accountRepository.existsByAccountNumber(null)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.creditAccount(request);
        });
    }

    @Test
    public void testDebitAccount_ExistingAccount_SufficientBalance() {

        transaction = Transaction.builder()
                .transactionType(TransactionType.DEBIT)
                .transactionAmount(BigDecimal.valueOf(0))
                .accountNumber("1")
                .build();

        transactionDto = TransactionDto.builder()
                .transactionType(TransactionType.DEBIT)
                .transactionAmount(BigDecimal.valueOf(0))
                .accountNumber("1")
                .build();

        CreditDebitRequest request = new CreditDebitRequest();
        request.setAccountNumber("1");
        request.setAmount(BigDecimal.valueOf(0));
        account.setAccountBalance(account.getAccountBalance().subtract(request.getAmount()));

        when(accountRepository.existsByAccountNumber(request.getAccountNumber())).thenReturn(true);
        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(account);
        doNothing().when(transactionService).saveTransaction(transactionDto);

        accountDto = accountService.debitAccount(request);

        assertEquals("1", accountDto.getAccountNumber());

        verify(accountRepository).existsByAccountNumber("1");
        verify(accountRepository).findByAccountNumber("1");
    }

    @Test
    public void testDebitAccount_ExistingAccount_InsufficientBalance() {
        CreditDebitRequest request = new CreditDebitRequest();
        request.setAccountNumber("1");
        request.setAmount(BigDecimal.valueOf(1000));
        when(accountRepository.existsByAccountNumber("1")).thenReturn(true);
        when(accountRepository.findByAccountNumber("1")).thenReturn(account);

        assertThrows(InsufficientBalanceException.class, () -> {
            accountService.debitAccount(request);
        });
    }

    @Test
    public void testDebitAccount_NonExistingAccount() {
        CreditDebitRequest request = new CreditDebitRequest();
        request.setAmount(BigDecimal.valueOf(0));
        when(accountRepository.existsByAccountNumber(null)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.debitAccount(request);
        });
    }

    @Test
    public void testTransfer_DestinationAccountNotExists() {
        TransferRequest request = new TransferRequest();
        when(accountRepository.existsByAccountNumber(null)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.transfer(request);
        });
    }

    @Test
    public void testTransfer_ExistingAccounts_SufficientBalance() {
        TransferRequest request = new TransferRequest();
        request.setFromAccount("1");
        request.setToAccount("1");
        request.setTransferAmount(BigDecimal.valueOf(1));

        account.setAccountBalance(account.getAccountBalance().add(request.getTransferAmount()));

        when(accountRepository.existsByAccountNumber(request.getToAccount())).thenReturn(true);
        when(accountRepository.findByAccountNumber(request.getFromAccount())).thenReturn(account);
        when(accountRepository.findByAccountNumber(request.getToAccount())).thenReturn(account);

        accountDto = accountService.transfer(request);

        assertEquals("1", accountDto.getAccountNumber());

        verify(accountRepository).existsByAccountNumber("1");
    }

    @Test
    public void testTransfer_ExistingAccounts_InsufficientBalance() {
        TransferRequest request = new TransferRequest();
        request.setFromAccount("1");
        request.setToAccount("2");
        request.setTransferAmount(BigDecimal.valueOf(1));

        account.setAccountBalance(account.getAccountBalance().subtract(request.getTransferAmount()));
        account.setAccountBalance(account.getAccountBalance().add(request.getTransferAmount()));

        when(accountRepository.existsByAccountNumber(request.getToAccount())).thenReturn(true);
        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(account);

        assertThrows(InsufficientBalanceException.class, () -> {
            accountService.transfer(request);
        });
    }

    @Test
    public void updateByAccountNumber_when_Account_number_does_not_exist() {

    }

    public void test_deleteByAccountNumber(String accountNumber) {

    }

//    @Test
//    public void testGetAll() {
//
//      User  user1 = User.builder()
//                .firstName("firstName2")
//                .lastName("lastName2")
//                .accountNumber("2")
//                .accountBalance(BigDecimal.ZERO)
//                .email("email2")
//                .createdAt(LocalDateTime.now().withNano(0))
//                .updatedAt(LocalDateTime.now().withNano(0))
//                .build();
//
//        BDDMockito.given(userRepository.findAll()).willReturn(List.of(user, user1));
//
//        List<UserDto> userDtoList = userService.getAll();
//
//        assertThat(userDtoList).isNotNull();
//        assertThat(userDtoList.size()).isEqualTo(2);
//    }



    @Test
    public void testUpdateExistingAccount() {

    }

}