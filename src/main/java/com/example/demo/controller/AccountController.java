package com.example.demo.controller;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.AccountService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    // ================= LOGIN =================
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        Account account = accountService.login(username, password);

        if (account != null) {
            session.setAttribute("user", account);

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Login successful! Welcome " + account.getFullName()
            );

            return "redirect:/dashboard";
        }

        redirectAttributes.addFlashAttribute(
                "error",
                "Invalid username or password"
        );

        return "redirect:/";
    }

    // ================= CREATE ACCOUNT =================
    @PostMapping("/create")
    public String createAccount(@RequestParam String username,
                                @RequestParam String fullName,
                                @RequestParam String password,
                                RedirectAttributes redirectAttributes) {

        accountService.createAccount(username, fullName, password);

        redirectAttributes.addFlashAttribute(
                "message",
                "Account created successfully! Please login."
        );

        return "redirect:/";
    }

    // ================= WITHDRAW =================
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber,
                           @RequestParam double amount,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) {

        String result = accountService.withdraw(accountNumber, amount);

        Account updated = accountRepository.findByAccountNumber(accountNumber);

        session.setAttribute("user", updated);

        redirectAttributes.addFlashAttribute("message", result);

        return "redirect:/dashboard";
    }

    // ================= DEPOSIT =================
    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber,
                          @RequestParam double amount,
                          RedirectAttributes redirectAttributes,
                          HttpSession session) {

        String result = accountService.deposit(accountNumber, amount);

        Account updated = accountRepository.findByAccountNumber(accountNumber);

        session.setAttribute("user", updated);

        redirectAttributes.addFlashAttribute("message", result);

        return "redirect:/dashboard";
    }

    // ================= TRANSFER =================
    @PostMapping("/transfer")
    public String transfer(@RequestParam String fromAccount,
                           @RequestParam String toAccount,
                           @RequestParam double amount,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) {

        String result = accountService.transfer(fromAccount, toAccount, amount);

        // refresh sender account (for dashboard display)
        Account updated = accountRepository.findByAccountNumber(fromAccount);

        session.setAttribute("user", updated);

        redirectAttributes.addFlashAttribute("message", result);

        return "redirect:/dashboard";
    }

    // ================= BALANCE =================
    @GetMapping("/balance/{accountNumber}")
    @ResponseBody
    public String getBalance(@PathVariable String accountNumber) {
        return accountService.getBalance(accountNumber);
    }
}