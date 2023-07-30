package kz.almaty.moneytransferservice.repository;

import kz.almaty.moneytransferservice.model.User;
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
public class UserRepositoryTestIT {
    private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer();


    @Autowired
    private UserRepository userRepository;

    @Test
    void existsByEmail() {
        User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .email("email")
                .accountNumber("123")
                .accountBalance(BigDecimal.valueOf(100))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);
        assertThat(savedUser).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(user);
    }

    @Test
    void existsByAccountNumber() {
        User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .email("email")
                .accountNumber("123")
                .accountBalance(BigDecimal.valueOf(100))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);
        assertThat(savedUser).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(user);
    }

    @Test
    void findByAccountNumber() {
        User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .email("email")
                .accountNumber("123")
                .accountBalance(BigDecimal.valueOf(100))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);
        assertThat(savedUser).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(user);

    }
}
