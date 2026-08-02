package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    private Long id;
    private String username;
    private String accountNumber;
    private String fullName;
    private String password;
    @Column(precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;
}