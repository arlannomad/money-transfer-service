package kz.almaty.moneytransferservice.service;

import kz.almaty.moneytransferservice.dto.CreditDebitRequest;
import kz.almaty.moneytransferservice.dto.PageDto;
import kz.almaty.moneytransferservice.dto.TransferRequest;
import kz.almaty.moneytransferservice.dto.UserDto;

import java.util.List;

public interface UserService {
    //    BankResponse addAccount(UserDto userRequest);
    UserDto addAccount(UserDto userDto);
    UserDto findByAccountNumber(CreditDebitRequest enquiryRequest);
    UserDto creditAccount(CreditDebitRequest request);
    UserDto debitAccount(CreditDebitRequest request);
    UserDto transfer(TransferRequest request);
    void deleteByAccountNumber(String accountNumber);
    UserDto updateByAccountNumber(String accountNumber, UserDto userDto);
    List<UserDto> getAll();
    PageDto getAllUsersByPages(int pageNumber, int pageSize, String sortBy, String sortDirection);
}
