package kz.almaty.moneytransferservice.repository;

import kz.almaty.moneytransferservice.enums.Status;
import kz.almaty.moneytransferservice.enums.TransactionType;
import kz.almaty.moneytransferservice.model.Transaction;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.math.BigDecimal;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@DataJpaTest
//@Testcontainers
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class TransactionRepositoryTestIT {
//    private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer();
//
//    @Autowired
//    private TransactionRepository userRepository;
//
//    @Test
//    void addTransaction() {
//        Transaction transaction = Transaction.builder()
//                .transactionType(TransactionType.TRANSFER)
//                .transactionAmount(BigDecimal.valueOf(100))
//                .accountNumber("1")
//                .status(Status.SUCCESS)
//                .build();
//        Transaction savedTransaction = userRepository.save(transaction);
//        assertThat(savedTransaction).usingRecursiveComparison().ignoringFields("transactionId")
//                .isEqualTo(transaction);
//    }
//}