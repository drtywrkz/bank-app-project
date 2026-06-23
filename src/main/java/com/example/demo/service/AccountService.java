package com.example.demo.service;

import com.example.demo.model.Account;
import com.example.demo.model.Transaction;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    // ================= LOGIN =================
    public Account login(String username, String password) {

        Account account = accountRepository.findByUsername(username);

        if (account != null && account.getPassword().equals(password)) {
            return account;
        }

        return null;
    }

    // ================= DEPOSIT =================
    public String deposit(String accountNumber, double amount) {

        Account account = accountRepository.findByAccountNumber(accountNumber);

        if (account == null) {
            return "Account not found";
        }

        if (amount <= 0) {
            return "Invalid amount";
        }

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction tx = Transaction.builder()
                .accountNumber(accountNumber)
                .transactionType("DEPOSIT")
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .build();

        transactionRepository.save(tx);

        return "Deposit successful";
    }

    // ================= WITHDRAW =================
    public String withdraw(String accountNumber, double amount) {

        Account account = accountRepository.findByAccountNumber(accountNumber);

        if (account == null) return "Account not found";

        if (account.getBalance() < amount) return "Insufficient balance";

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction tx = Transaction.builder()
                .accountNumber(accountNumber)
                .transactionType("WITHDRAW")
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .build();

        transactionRepository.save(tx);

        return "Withdraw successful";
    }

    // ================= TRANSFER =================
    public String transfer(String fromAccount, String toAccount, double amount) {

        Account sender = accountRepository.findByAccountNumber(fromAccount);

        Account receiver = accountRepository.findByAccountNumber(toAccount);

        if (sender == null) return "Sender account not found";
        if (receiver == null) return "Receiver account not found";

        if (amount <= 0) return "Invalid amount";

        if (sender.getBalance() < amount) return "Insufficient balance";

        // deduct sender
        sender.setBalance(sender.getBalance() - amount);
        accountRepository.save(sender);

        // add receiver
        receiver.setBalance(receiver.getBalance() + amount);
        accountRepository.save(receiver);

        Transaction tx1 = Transaction.builder()
                .accountNumber(fromAccount)
                .transactionType("TRANSFER_OUT")
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .build();

        Transaction tx2 = Transaction.builder()
                .accountNumber(toAccount)
                .transactionType("TRANSFER_IN")
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .build();

        transactionRepository.save(tx1);
        transactionRepository.save(tx2);

        return "Transfer successful";
    }

    // ================= CREATE ACCOUNT =================
    public String createAccount(String username, String fullName, String password) {

        String accountNumber = generateAccountNumber();

        Account account = Account.builder()
                .username(username)
                .fullName(fullName)
                .password(password)
                .accountNumber(accountNumber)
                .balance(0)
                .build();

        accountRepository.save(account);

        return "Account created successfully. Account Number: " + accountNumber;
    }

    // ================= BALANCE =================
    public String getBalance(String accountNumber) {

        Account account = accountRepository.findByAccountNumber(accountNumber);

        if (account == null) {
            return "Account not found";
        }

        return "Current balance: " + account.getBalance();
    }

    // ================= GENERATE ACCOUNT NUMBER =================
    private String generateAccountNumber() {
        return "ACC" + (100000 + new java.util.Random().nextInt(900000));
    }
}