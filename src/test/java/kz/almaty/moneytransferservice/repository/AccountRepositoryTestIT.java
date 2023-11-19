package kz.almaty.moneytransferservice.repository;

import kz.almaty.moneytransferservice.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTestIT {
    private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer();


    @Autowired
    private AccountRepository accountRepository;

    @Test
    void existsByEmail() {
        Account user = Account.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .accountNumber("123")
                .accountBalance(BigDecimal.valueOf(100))
                .createdAt(LocalDateTime.now().withNano(0))
                .updatedAt(LocalDateTime.now().withNano(0))
                .build();
        Account savedUser = accountRepository.save(user);
        assertThat(savedUser).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(user);
    }

    @Test
    void existsByAccountNumber() {
        Account user = Account.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .accountNumber("123")
                .accountBalance(BigDecimal.valueOf(100))
                .createdAt(LocalDateTime.now().withNano(0))
                .updatedAt(LocalDateTime.now().withNano(0))
                .build();
        Account savedUser = accountRepository.save(user);
        assertThat(savedUser).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(user);
    }

    @Test
    void findByAccountNumber() {
        Account user = Account.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .accountNumber("123")
                .accountBalance(BigDecimal.valueOf(100))
                .createdAt(LocalDateTime.now().withNano(0))
                .updatedAt(LocalDateTime.now().withNano(0))
                .build();
        Account savedUser = accountRepository.save(user);
        assertThat(savedUser).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(user);

    }
}
