package org.kea.easyscope.controller;

import jakarta.servlet.http.HttpSession;
import org.kea.easyscope.model.Account;
import org.kea.easyscope.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = {"", "/"})
    public String showAccount(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");

        // Set a variable to check if account should have access to creat projects ...
        if (account == null) {
            return "redirect:/login";
        } else {

            boolean canCreateProject = (account.getAccountType() == Account.AccountType.PROJECT_MANAGER) || (account.getAccountType() == Account.AccountType.ADMIN);
            // First is for TRUE ...
            if (account != null && canCreateProject) {
                model.addAttribute("account", account);
                model.addAttribute("canCreateProject", canCreateProject);
                return "account";
            }
            // Then if FALSE ...
            else {
                model.addAttribute("account", account);
                return "account";
            }
        }
    }

    @GetMapping(value = {"/edit", "/edit/"})
    public String showEditAccount(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");

        if (account != null && account.getAccountType() == Account.AccountType.ADMIN) {
            List<Account> otherAccounts = accountService.getAllNonAdminAccounts(account.getAccountID());
            model.addAttribute("otherAccounts", otherAccounts);
            model.addAttribute("account", account);
            model.addAttribute("accountType", account.getAccountType().name());  // Add accountType as a string
            return "accountedit";
        } else if (account != null && account.getAccountType() != Account.AccountType.ADMIN){
            model.addAttribute("account", account);
            return "accountedit";
        } else {
            return "redirect:/login";
        }
    }
}
