package kz.almaty.moneytransferservice.controller;

import kz.almaty.moneytransferservice.dto.CreditDebitRequest;
import kz.almaty.moneytransferservice.dto.TransferRequest;
import kz.almaty.moneytransferservice.dto.UserDto;
import kz.almaty.moneytransferservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //	http://localhost:8181/swagger-ui/index.html#/
    @PostMapping
    public UserDto addAccount(@RequestBody UserDto userDto) {
        return userService.addAccount(userDto);
    }

    @GetMapping("/getBalanceByAccountNumber")
    public UserDto findByAccountNumber(@RequestBody CreditDebitRequest request) {
        return userService.findByAccountNumber(request);
    }

    @PostMapping("/creditAccountByAccountNumber")
    public UserDto creditAccount(@RequestBody CreditDebitRequest request) {
        return userService.creditAccount(request);
    }

    @PostMapping("/debitAccountByAccountNumber")
    public UserDto debitAccount(@RequestBody CreditDebitRequest request) {
        return userService.debitAccount(request);
    }

    @PostMapping("/transferByAccountNumbers")
    public UserDto transfer(@RequestBody TransferRequest request) {
        return userService.transfer(request);
    }

    @PutMapping("/updateByAccountNumber/{accountNumber}")
    public UserDto updateByAccountNumber(@PathVariable("accountNumber") String accountNumber, @RequestBody UserDto userDto) {
        return userService.updateByAccountNumber(accountNumber, userDto);
    }

    @DeleteMapping("/deleteByAccountNumber/{accountNumber}")
    public void deleteByAccountNumber(@PathVariable("accountNumber") String accountNumber) {
        userService.deleteByAccountNumber(accountNumber);
    }

    @GetMapping("/getAllAccounts")
    public List<UserDto> getAllUsers() {
        return userService.getAll();
    }
}
