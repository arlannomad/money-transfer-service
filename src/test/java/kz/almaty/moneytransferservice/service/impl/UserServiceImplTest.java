package kz.almaty.moneytransferservice.service.impl;

import kz.almaty.moneytransferservice.dto.CreditDebitRequest;
import kz.almaty.moneytransferservice.dto.TransactionDto;
import kz.almaty.moneytransferservice.dto.TransferRequest;
import kz.almaty.moneytransferservice.dto.UserDto;
import kz.almaty.moneytransferservice.enums.TransactionType;
import kz.almaty.moneytransferservice.exception.already_exists_exception.ResourceAlreadyExistsException;
import kz.almaty.moneytransferservice.exception.insufficient_balance.InsufficientBalanceException;
import kz.almaty.moneytransferservice.exception.not_found_exception.ResourceNotFoundException;
import kz.almaty.moneytransferservice.model.Transaction;
import kz.almaty.moneytransferservice.model.User;
import kz.almaty.moneytransferservice.repository.UserRepository;
import kz.almaty.moneytransferservice.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    TransactionService transactionService;
    private User user;
    private UserDto userDto;

    private Transaction transaction;
    private TransactionDto transactionDto;

    @BeforeEach
    public void setUp() {
        userDto = UserDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .accountNumber("1")
                .accountBalance(BigDecimal.valueOf(100))
                .email("email")
                .createdAt(LocalDateTime.now().withNano(0))
                .updatedAt(LocalDateTime.now().withNano(0))
                .build();

        user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .accountNumber("1")
                .accountBalance(BigDecimal.ZERO)
                .email("email")
                .createdAt(LocalDateTime.now().withNano(0))
                .updatedAt(LocalDateTime.now().withNano(0))
                .build();
    }

    @Test
    public void testAddAccount_AccountDoesNotExist() {

        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto savedUserDto = userService.addAccount(userDto);

        assertThat(savedUserDto).isNotNull();
        assertEquals("firstName", savedUserDto.getFirstName());
        assertEquals("lastName", savedUserDto.getLastName());

        verify(userRepository).existsByEmail(userDto.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testAddAccount_AccountAlreadyExists() {

        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            userService.addAccount(userDto);
        });

    }

    @Test
    public void testFindByAccountNumber_AccountExists() {
        CreditDebitRequest request = new CreditDebitRequest();
        request.setAccountNumber("1");

        when(userRepository.existsByAccountNumber(request.getAccountNumber())).thenReturn(true);
        when(userRepository.findByAccountNumber(user.getAccountNumber())).thenReturn(user);

        UserDto dto = userService.findByAccountNumber(request);

        assertEquals("1", dto.getAccountNumber());

        verify(userRepository).existsByAccountNumber("1");
        verify(userRepository).findByAccountNumber("1");
    }

    @Test
    public void testBalanceEnquiry_AccountNotExists() {

        CreditDebitRequest request = new CreditDebitRequest();
        when(userRepository.existsByAccountNumber(null)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.findByAccountNumber(request);
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
        user.setAccountBalance(user.getAccountBalance().add(request.getAmount()));

        when(userRepository.existsByAccountNumber(request.getAccountNumber())).thenReturn(true);
        when(userRepository.findByAccountNumber(user.getAccountNumber())).thenReturn(user);
        doNothing().when(transactionService).saveTransaction(transactionDto);

        userDto = userService.creditAccount(request);

        assertEquals("1", userDto.getAccountNumber());

        verify(userRepository).existsByAccountNumber("1");
        verify(userRepository).findByAccountNumber("1");
    }

    @Test
    public void testCreditAccount_NonExistingAccount() {
        CreditDebitRequest request = new CreditDebitRequest();
        when(userRepository.existsByAccountNumber(null)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.creditAccount(request);
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
        user.setAccountBalance(user.getAccountBalance().subtract(request.getAmount()));

        when(userRepository.existsByAccountNumber(request.getAccountNumber())).thenReturn(true);
        when(userRepository.findByAccountNumber(user.getAccountNumber())).thenReturn(user);
        doNothing().when(transactionService).saveTransaction(transactionDto);

        userDto = userService.debitAccount(request);

        assertEquals("1", userDto.getAccountNumber());

        verify(userRepository).existsByAccountNumber("1");
        verify(userRepository).findByAccountNumber("1");
    }

    @Test
    public void testDebitAccount_ExistingAccount_InsufficientBalance() {
        CreditDebitRequest request = new CreditDebitRequest();
        request.setAccountNumber("1");
        request.setAmount(BigDecimal.valueOf(1000));
        when(userRepository.existsByAccountNumber("1")).thenReturn(true);
        when(userRepository.findByAccountNumber("1")).thenReturn(user);

        assertThrows(InsufficientBalanceException.class, () -> {
            userService.debitAccount(request);
        });
    }

    @Test
    public void testDebitAccount_NonExistingAccount() {
        CreditDebitRequest request = new CreditDebitRequest();
        request.setAmount(BigDecimal.valueOf(0));
        when(userRepository.existsByAccountNumber(null)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.debitAccount(request);
        });
    }

    @Test
    public void testTransfer_DestinationAccountNotExists() {
        TransferRequest request = new TransferRequest();
        when(userRepository.existsByAccountNumber(null)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.transfer(request);
        });
    }

    @Test
    public void testTransfer_ExistingAccounts_SufficientBalance() {
        TransferRequest request = new TransferRequest();
        request.setFromAccount("1");
        request.setToAccount("1");
        request.setTransferAmount(BigDecimal.valueOf(1));

        user.setAccountBalance(user.getAccountBalance().add(request.getTransferAmount()));

        when(userRepository.existsByAccountNumber(request.getToAccount())).thenReturn(true);
        when(userRepository.findByAccountNumber(request.getFromAccount())).thenReturn(user);
        when(userRepository.findByAccountNumber(request.getToAccount())).thenReturn(user);

        userDto = userService.transfer(request);

        assertEquals("1", userDto.getAccountNumber());

        verify(userRepository).existsByAccountNumber("1");
    }

    @Test
    public void testTransfer_ExistingAccounts_InsufficientBalance() {
        TransferRequest request = new TransferRequest();
        request.setFromAccount("1");
        request.setToAccount("2");
        request.setTransferAmount(BigDecimal.valueOf(1));

        user.setAccountBalance(user.getAccountBalance().subtract(request.getTransferAmount()));
        user.setAccountBalance(user.getAccountBalance().add(request.getTransferAmount()));

        when(userRepository.existsByAccountNumber(request.getToAccount())).thenReturn(true);
        when(userRepository.findByAccountNumber(user.getAccountNumber())).thenReturn(user);

        assertThrows(InsufficientBalanceException.class, () -> {
            userService.transfer(request);
        });
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

}