package com.example.demo.controller;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import com.example.demo.model.Account;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final AccountRepository accountRepository;

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

        // 🔥 ALWAYS GET FRESH DATA
        Account account = accountRepository.findById(sessionAccount.getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // 🔥 update session too (important)
        session.setAttribute("user", account);

        model.addAttribute("account", account);

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