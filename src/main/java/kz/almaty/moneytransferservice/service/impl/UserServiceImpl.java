package kz.almaty.moneytransferservice.service.impl;

import kz.almaty.moneytransferservice.dto.*;
import kz.almaty.moneytransferservice.enums.TransactionType;
import kz.almaty.moneytransferservice.exception.already_exists_exception.ResourceAlreadyExistsException;
import kz.almaty.moneytransferservice.exception.insufficient_balance.InsufficientBalanceException;
import kz.almaty.moneytransferservice.exception.not_found_exception.ResourceNotFoundException;
import kz.almaty.moneytransferservice.mapper.UserMapper;
import kz.almaty.moneytransferservice.model.User;
import kz.almaty.moneytransferservice.repository.UserRepository;
import kz.almaty.moneytransferservice.service.TransactionService;
import kz.almaty.moneytransferservice.service.UserService;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TransactionService transactionService;


    @Override
    public UserDto addAccount(UserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email: (" + userDto.getEmail() + ") already exists");
        } else {
            User newUser = UserMapper.mapToUser(userDto);
            User user = userRepository.save(newUser);

            return UserMapper.mapToDto(user);
        }
    }

    @Override
    public UserDto findByAccountNumber(CreditDebitRequest request) {
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExists) {
            throw new ResourceNotFoundException("Account Number: (" + request.getAccountNumber() + ") doesn't exist");
        }
        User user = userRepository.findByAccountNumber(request.getAccountNumber());

        return UserMapper.mapToDto(user);
    }

    @Override
    public UserDto creditAccount(CreditDebitRequest request) {
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExists) {
            throw new ResourceNotFoundException("Account Number: (" + request.getAccountNumber() + ") doesn't exist");
        } else {
            User user = userRepository.findByAccountNumber(request.getAccountNumber());
            user.setAccountBalance(user.getAccountBalance().add(request.getAmount()));
            userRepository.save(user);

            //save Transaction
            TransactionDto transactionDto = TransactionDto.builder()
                    .transactionType(TransactionType.CREDIT)
                    .transactionAmount(request.getAmount())
                    .accountNumber(user.getAccountNumber())
                    .build();

            transactionService.saveTransaction(transactionDto);

            return UserMapper.mapToDto(user);
        }
    }

    @Override
    public UserDto debitAccount(CreditDebitRequest request) {
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExists) {
            throw new ResourceNotFoundException("Account Number: (" + request.getAccountNumber() + ") doesn't exist");
        } else {
            User user = userRepository.findByAccountNumber(request.getAccountNumber());

            BigDecimal availableBalance = user.getAccountBalance();
            BigDecimal debitAmount = request.getAmount();

            if ((availableBalance.compareTo(debitAmount) < 0)) {
                throw new InsufficientBalanceException("Insufficient balance");
            } else {
                user.setAccountBalance(user.getAccountBalance().subtract(request.getAmount()));
                userRepository.save(user);

                //save Transaction
                TransactionDto transactionDto = TransactionDto.builder()
                        .transactionType(TransactionType.DEBIT)
                        .transactionAmount(request.getAmount())
                        .accountNumber(user.getAccountNumber())
                        .build();

                transactionService.saveTransaction(transactionDto);

                return UserMapper.mapToDto(user);
            }
        }
    }

    @Override
    public UserDto transfer(TransferRequest request) {
        boolean isDestinationAccountExists = userRepository.existsByAccountNumber(request.getToAccount());
        if (!isDestinationAccountExists) {
            throw new ResourceNotFoundException("Account Number: (" + request.getToAccount() + ") doesn't exist");
        }

        User fromUserAccount = userRepository.findByAccountNumber(request.getFromAccount());
        if (request.getTransferAmount().compareTo(fromUserAccount.getAccountBalance()) > 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        fromUserAccount.setAccountBalance(fromUserAccount.getAccountBalance()
                .subtract(request.getTransferAmount()));
        userRepository.save(fromUserAccount);

        User user = userRepository.findByAccountNumber(request.getToAccount());
        user.setAccountBalance(user.getAccountBalance().add(request.getTransferAmount()));
        userRepository.save(user);

        //save Transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType(TransactionType.TRANSFER)
                .transactionAmount(request.getTransferAmount())
                .accountNumber(user.getAccountNumber())
                .build();

        transactionService.saveTransaction(transactionDto);

        return UserMapper.mapToDto(user);
    }

    @Override
    public void deleteByAccountNumber(String accountNumber) {
        boolean isAccountExists = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExists) {
            throw new ResourceNotFoundException("Account Number: (" + accountNumber + ") doesn't exist");
        }
        User user = userRepository.findByAccountNumber(accountNumber);
        userRepository.delete(user);
    }

    @Override
    public UserDto updateByAccountNumber(String accountNumber, UserDto userDto) {
        boolean isAccountExists = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExists) {
            throw new ResourceNotFoundException("Account Number: (" + accountNumber + ") doesn't exist");
        }
        User user = userRepository.findByAccountNumber(accountNumber);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        User updatedUser = userRepository.save(user);

        return UserMapper.mapToDto(updatedUser);
    }

    @Override
    public PageDto getAllUsersByPages(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> posts = userRepository.findAll(pageable);
        List<User> postList = posts.getContent();
        List<UserDto> content = postList.stream().map(UserMapper::mapToDto).collect(Collectors.toList());
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
