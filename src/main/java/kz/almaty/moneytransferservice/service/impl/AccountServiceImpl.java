package kz.almaty.moneytransferservice.service.impl;

import kz.almaty.moneytransferservice.dto.*;
import kz.almaty.moneytransferservice.enums.TransactionType;
import kz.almaty.moneytransferservice.exception.already_exists_exception.ResourceAlreadyExistsException;
import kz.almaty.moneytransferservice.exception.insufficient_balance.InsufficientBalanceException;
import kz.almaty.moneytransferservice.exception.not_found_exception.ResourceNotFoundException;
import kz.almaty.moneytransferservice.mapper.AccountMapper;
import kz.almaty.moneytransferservice.model.Account;
import kz.almaty.moneytransferservice.repository.AccountRepository;
import kz.almaty.moneytransferservice.service.TransactionService;
import kz.almaty.moneytransferservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;


    @Override
    public AccountDto addAccount(AccountDto accountDto) {

        if (accountRepository.existsByAccountNumber(accountDto.getAccountNumber())) {
            throw new ResourceAlreadyExistsException("Account Number: (" + accountDto.getAccountNumber()+ ") already exists");
        } else {
            Account newAccount = AccountMapper.mapToAccount(accountDto);
            Account account = accountRepository.save(newAccount);

            return AccountMapper.mapToDto(account);
        }
    }

    @Override
    public AccountDto findByAccountNumber(CreditDebitRequest request) {
        boolean isAccountExists = accountRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExists) {
            throw new ResourceNotFoundException("Account Number: (" + request.getAccountNumber() + ") doesn't exist");
        }
        Account user = accountRepository.findByAccountNumber(request.getAccountNumber());

        return AccountMapper.mapToDto(user);
    }

    @Override
    public AccountDto creditAccount(CreditDebitRequest request) {
        boolean isAccountExists = accountRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExists) {
            throw new ResourceNotFoundException("Account Number: (" + request.getAccountNumber() + ") doesn't exist");
        } else {
            Account user = accountRepository.findByAccountNumber(request.getAccountNumber());
            user.setAccountBalance(user.getAccountBalance().add(request.getAmount()));
            accountRepository.save(user);

            //save Transaction
            TransactionDto transactionDto = TransactionDto.builder()
                    .transactionType(TransactionType.CREDIT)
                    .transactionAmount(request.getAmount())
                    .accountNumber(user.getAccountNumber())
                    .build();

            transactionService.saveTransaction(transactionDto);

            return AccountMapper.mapToDto(user);
        }
    }

    @Override
    public AccountDto debitAccount(CreditDebitRequest request) {
        boolean isAccountExists = accountRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExists) {
            throw new ResourceNotFoundException("Account Number: (" + request.getAccountNumber() + ") doesn't exist");
        } else {
            Account user = accountRepository.findByAccountNumber(request.getAccountNumber());

            BigDecimal availableBalance = user.getAccountBalance();
            BigDecimal debitAmount = request.getAmount();

            if ((availableBalance.compareTo(debitAmount) < 0)) {
                throw new InsufficientBalanceException("Insufficient balance");
            } else {
                user.setAccountBalance(user.getAccountBalance().subtract(request.getAmount()));
                accountRepository.save(user);

                //save Transaction
                TransactionDto transactionDto = TransactionDto.builder()
                        .transactionType(TransactionType.DEBIT)
                        .transactionAmount(request.getAmount())
                        .accountNumber(user.getAccountNumber())
                        .build();

                transactionService.saveTransaction(transactionDto);

                return AccountMapper.mapToDto(user);
            }
        }
    }

    @Override
    public AccountDto transfer(TransferRequest request) {
        boolean isDestinationAccountExists = accountRepository.existsByAccountNumber(request.getToAccount());
        if (!isDestinationAccountExists) {
            throw new ResourceNotFoundException("Account Number: (" + request.getToAccount() + ") doesn't exist");
        }

        Account fromUserAccount = accountRepository.findByAccountNumber(request.getFromAccount());
        if (request.getTransferAmount().compareTo(fromUserAccount.getAccountBalance()) > 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        fromUserAccount.setAccountBalance(fromUserAccount.getAccountBalance()
                .subtract(request.getTransferAmount()));
        accountRepository.save(fromUserAccount);

        Account user = accountRepository.findByAccountNumber(request.getToAccount());
        user.setAccountBalance(user.getAccountBalance().add(request.getTransferAmount()));
        accountRepository.save(user);

        //save Transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType(TransactionType.TRANSFER)
                .transactionAmount(request.getTransferAmount())
                .accountNumber(user.getAccountNumber())
                .build();

        transactionService.saveTransaction(transactionDto);

        return AccountMapper.mapToDto(user);
    }

    @Override
    public void deleteByAccountNumber(String accountNumber) {
        boolean isAccountExists = accountRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExists) {
            throw new ResourceNotFoundException("Account Number: (" + accountNumber + ") doesn't exist");
        }
        Account user = accountRepository.findByAccountNumber(accountNumber);
        accountRepository.delete(user);
    }

    @Override
    public AccountDto updateByAccountNumber(String accountNumber, AccountDto accountDto) {
        boolean isAccountExists = accountRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExists) {
            throw new ResourceNotFoundException("Account Number: (" + accountNumber + ") doesn't exist");
        }
        Account user = accountRepository.findByAccountNumber(accountNumber);
        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        Account updatedUser = accountRepository.save(user);

        return AccountMapper.mapToDto(updatedUser);
    }

//    @Override
//    public List<UserDto> getAll() {
//        List<User> users = userRepository.findAll();
//        return users.stream().map(UserMapper::mapToDto).toList();
//    }

    @Override
    public PageDto getAllUsersByPages(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Account> posts = accountRepository.findAll(pageable);
        List<Account> postList = posts.getContent();
        List<AccountDto> content = postList.stream().map(AccountMapper::mapToDto).collect(Collectors.toList());
        return PageDto.builder()
                .content(content)
                .pageNumber(posts.getNumber())
                .pageSize(posts.getSize())
                .pageSize(posts.getSize())
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .last(posts.isLast())
                .build();
    }

}
