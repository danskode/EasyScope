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

    @GetMapping
    public String showAccount(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);

        System.out.println(account.toString());
        return "account";
    }

    //

    // if not logged in show login form ...
    // if logged in show user page ...

}
