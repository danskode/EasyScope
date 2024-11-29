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

    /* Se i account.html
    @GetMapping
    public String showAccount(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        return "account";
    }*/

    // This is an overview of the identified visitor's (session's) account page ...
    @GetMapping
    public String showAccount(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);

        System.out.println(account.toString());
        return "account";
    }

    // This lets all users edit their own accounts (accountedit.html) ...
    @GetMapping("/edit")
    public String editAccount(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        return "accountedit";
    }

    // Now, let's only let admins edit the accountType for all non-admin accounts ...
//    @GetMapping("/edit")
//    public String editAccountTypeOnNonAdmins(HttpSession session, Model model) {
//        // Fetch the current user's account from the session
//        Account account = (Account) session.getAttribute("account");
//
//        if (account != null && account.getAccountType() == Account.AccountType.ADMIN) {
//            // Only if the logged-in user is an admin, we proceed with displaying non-admin accounts
//            List<Account> otherAccounts = accountService.getAllNonAdminAccounts();
//
//            // Add the list of other non-admin accounts to the model
//            model.addAttribute("otherAccounts", otherAccounts);
//
//            // Add the current admin account to the model
//            model.addAttribute("account", account);
//
//            return "accountedit";  // Return the edit page
//        } else {
//            // If the user is not an admin, return the page without non-admin account options
//            model.addAttribute("account", account);
//            return "accountedit";  // Return the same edit page
//        }
//    }

}
