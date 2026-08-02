package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private String transactionType;
    @Column(precision = 19, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;
    private LocalDateTime timestamp;
}