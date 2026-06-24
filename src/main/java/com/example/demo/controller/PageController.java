package com.example.demo.controller;

import com.example.demo.model.Account;
import com.example.demo.model.Transaction;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TransactionRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        Account sessionAccount = (Account) session.getAttribute("user");

        if (sessionAccount == null) {
            return "redirect:/";
        }

        // Get fresh account data
        Account account = accountRepository.findById(sessionAccount.getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        session.setAttribute("user", account);

        // Transaction history
        List<Transaction> transactions =
                transactionRepository.findByAccountNumberOrderByTimestampDesc(
                        account.getAccountNumber()
                );

        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);

        return "dashboard";
    }

    @GetMapping("/deposit")
    public String deposit(HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/";
        return "deposit";
    }

    @GetMapping("/withdraw")
    public String withdraw(HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/";
        return "withdraw";
    }

    @GetMapping("/transfer")
    public String transfer(HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/";
        return "transfer";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}