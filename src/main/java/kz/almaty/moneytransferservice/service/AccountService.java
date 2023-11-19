package kz.almaty.moneytransferservice.service;

import kz.almaty.moneytransferservice.dto.CreditDebitRequest;
import kz.almaty.moneytransferservice.dto.PageDto;
import kz.almaty.moneytransferservice.dto.TransferRequest;
import kz.almaty.moneytransferservice.dto.AccountDto;

public interface AccountService {
    //    BankResponse addAccount(UserDto userRequest);
    AccountDto addAccount(AccountDto accountDto);
    AccountDto findByAccountNumber(CreditDebitRequest enquiryRequest);
    AccountDto creditAccount(CreditDebitRequest request);
    AccountDto debitAccount(CreditDebitRequest request);
    AccountDto transfer(TransferRequest request);
    void deleteByAccountNumber(String accountNumber);
    AccountDto updateByAccountNumber(String accountNumber, AccountDto accountDto);
//    List<UserDto> getAll();
    PageDto getAllUsersByPages(int pageNumber, int pageSize, String sortBy, String sortDirection);
}
