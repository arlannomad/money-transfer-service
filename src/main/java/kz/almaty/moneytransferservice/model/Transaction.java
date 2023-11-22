package kz.almaty.moneytransferservice.model;

import jakarta.persistence.*;
import kz.almaty.moneytransferservice.enums.Status;
import kz.almaty.moneytransferservice.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transactions")
public class Transaction {
    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private String transactionId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private TransactionType transactionType;
    private BigDecimal transactionAmount;
    private String accountNumber;
    private Status status;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
