package org.kea.easyscope.controller;

import jakarta.servlet.http.HttpSession;
import org.kea.easyscope.model.Account;
import org.kea.easyscope.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = {"/edit", "/edit/"})
    public String showEditAccount(HttpSession session,
                                  Model model) {
        Account account = (Account) session.getAttribute("account");
        // If account is not found in the session, throw an IllegalArgumentException
        if (account != null) {
            model.addAttribute("account", account);
            return "accountedit";
        } else {
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }
    }

    @PostMapping("/edit")
    public String editAccount(@RequestParam("newAccountName") String newAccountName,
                              HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        // If account is not found in session, throw IllegalArgumentException
        if (account != null) {
            int accountID = account.getAccountID();
            accountService.updateOwnAccount(newAccountName, accountID);
            account.setAccountName(newAccountName);
            return "redirect:/";
        } else {
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }
    }
}
