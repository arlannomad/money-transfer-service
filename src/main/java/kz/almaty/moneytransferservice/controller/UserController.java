package kz.almaty.moneytransferservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kz.almaty.moneytransferservice.dto.CreditDebitRequest;
import kz.almaty.moneytransferservice.dto.PageDto;
import kz.almaty.moneytransferservice.dto.TransferRequest;
import kz.almaty.moneytransferservice.dto.UserDto;
import kz.almaty.moneytransferservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kz.almaty.moneytransferservice.utils.AppConstants.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //	http://localhost:8181/swagger-ui/index.html#/
    @Operation(summary = "Add An Account REST API")
    @PostMapping
    public ResponseEntity<UserDto> addAccount(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.addAccount(userDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get Balance By Account Number REST API")
    @GetMapping("/getBalanceByAccountNumber")
    public ResponseEntity<UserDto> findByAccountNumber(@RequestBody CreditDebitRequest request) {
        return new ResponseEntity<>(userService.findByAccountNumber(request), HttpStatus.OK);
    }

    @Operation(summary = "Credit An Account By Account Number REST API")
    @PostMapping("/creditAccountByAccountNumber")
    public ResponseEntity<UserDto> creditAccount(@RequestBody CreditDebitRequest request) {
        return new ResponseEntity<>(userService.creditAccount(request), HttpStatus.OK);
    }

    @Operation(summary = "Debit An Account By Account Number REST API")
    @PostMapping("/debitAccountByAccountNumber")
    public ResponseEntity<UserDto> debitAccount(@RequestBody CreditDebitRequest request) {
        return new ResponseEntity<>(userService.debitAccount(request), HttpStatus.OK);
    }

    @Operation(summary = "Transfer Money From One Account To Another By Account Numbers REST API")
    @PostMapping("/transferByAccountNumbers")
    public ResponseEntity<UserDto> transfer(@RequestBody TransferRequest request) {
        return new ResponseEntity<>(userService.transfer(request), HttpStatus.OK);
    }

    @Operation(summary = "Update An Account By Account Numbers REST API")
    @PutMapping("/updateByAccountNumber/{accountNumber}")
    public ResponseEntity<UserDto> updateByAccountNumber(@PathVariable("accountNumber") String accountNumber, @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.updateByAccountNumber(accountNumber, userDto), HttpStatus.OK);
    }

    @Operation(summary = "Delete An Account By Account Numbers REST API")
    @DeleteMapping("/deleteByAccountNumber/{accountNumber}")
    public ResponseEntity<String> deleteByAccountNumber(@PathVariable("accountNumber") String accountNumber) {
        userService.deleteByAccountNumber(accountNumber);
        return new ResponseEntity<>("Account Deleted Successfully", HttpStatus.OK);
    }

    @Operation(summary = "Find And Sort All Accounts REST API")
    @ApiResponse(responseCode = "200",
            description = "Http Status 200 OK")
    @GetMapping("/getAllAccountsByPages")
    public ResponseEntity<PageDto> getAllUsersByPages(@RequestParam(value = "pageNumber", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
                                                      @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                      @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
                                                      @RequestParam(value = "sortDirection", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDirection) {
        return new ResponseEntity<>(userService.getAllUsersByPages(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }
}
