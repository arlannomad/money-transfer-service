package kz.almaty.moneytransferservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kz.almaty.moneytransferservice.dto.CreditDebitRequest;
import kz.almaty.moneytransferservice.dto.PageDto;
import kz.almaty.moneytransferservice.dto.TransferRequest;
import kz.almaty.moneytransferservice.dto.AccountDto;
import kz.almaty.moneytransferservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static kz.almaty.moneytransferservice.utils.AppConstants.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    //	http://localhost:8181/swagger-ui/index.html#/
    @Operation(summary = "Add An Account REST API")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AccountDto> addAccount(@RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(accountService.addAccount(accountDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get Balance By Account Number REST API")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getBalanceByAccountNumber")
    public ResponseEntity<AccountDto> findByAccountNumber(@RequestBody CreditDebitRequest request) {
        return new ResponseEntity<>(accountService.findByAccountNumber(request), HttpStatus.OK);
    }

    @Operation(summary = "Credit An Account By Account Number REST API")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/creditAccountByAccountNumber")
    public ResponseEntity<AccountDto> creditAccount(@RequestBody CreditDebitRequest request) {
        return new ResponseEntity<>(accountService.creditAccount(request), HttpStatus.OK);
    }

    @Operation(summary = "Debit An Account By Account Number REST API")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/debitAccountByAccountNumber")
    public ResponseEntity<AccountDto> debitAccount(@RequestBody CreditDebitRequest request) {
        return new ResponseEntity<>(accountService.debitAccount(request), HttpStatus.OK);
    }

    @Operation(summary = "Transfer Money From One Account To Another By Account Numbers REST API")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/transferByAccountNumbers")
    public ResponseEntity<AccountDto> transfer(@RequestBody TransferRequest request) {
        return new ResponseEntity<>(accountService.transfer(request), HttpStatus.OK);
    }

    @Operation(summary = "Update An Account By Account Numbers REST API")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateByAccountNumber/{accountNumber}")
    public ResponseEntity<AccountDto> updateByAccountNumber(@PathVariable("accountNumber") String accountNumber, @RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(accountService.updateByAccountNumber(accountNumber, accountDto), HttpStatus.OK);
    }

    @Operation(summary = "Delete An Account By Account Numbers REST API")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteByAccountNumber/{accountNumber}")
    public ResponseEntity<String> deleteByAccountNumber(@PathVariable("accountNumber") String accountNumber) {
        accountService.deleteByAccountNumber(accountNumber);
        return new ResponseEntity<>("Account Deleted Successfully", HttpStatus.OK);
    }

//    @GetMapping("/getAllAccounts")
//    public List<UserDto> getAllUsers() {
//        return userService.getAll();
//    }

    @Operation(summary = "Find And Sort All Accounts REST API")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponse(responseCode = "200",
            description = "Http Status 200 OK")
    @GetMapping("/getAllAccountsByPages")
    public ResponseEntity<PageDto> getAllUsersByPages(@RequestParam(value = "pageNumber", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
                                                      @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                      @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
                                                      @RequestParam(value = "sortDirection", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDirection) {
        return new ResponseEntity<>(accountService.getAllUsersByPages(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }
}
