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

        boolean canCreateProject = account != null &&
                (account.getAccountType() == Account.AccountType.PROJECT_MANAGER ||
                        account.getAccountType() == Account.AccountType.ADMIN);

        // Add the account and the flag to the model
        model.addAttribute("account", account);
        model.addAttribute("canCreateProject", canCreateProject);

        System.out.println(account.toString());
        return "account";
    }
}
