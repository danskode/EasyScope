package org.kea.easyscope.controller;

import org.kea.easyscope.model.Account;
import org.kea.easyscope.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public string account(@PathVariable int id, Model model) {
        Account account = accountService.getAccountFromID(id);

        model.addAttribute("account", account);

        return "account";
    }
}
