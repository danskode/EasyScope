package org.kea.easyscope.controller;

import jakarta.servlet.http.HttpSession;
import org.kea.easyscope.model.Account;
import org.kea.easyscope.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/adm")
public class AdminController {

    private final AccountService accountService;

    public AdminController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = {"/accounts/list","/accounts/list/"})
    public String adminAccounts(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");

        if (account != null && account.getAccountType() == Account.AccountType.ADMIN) {
            // Get a list of all other accounts which are not admins ...
            List<Account> otherAccounts = accountService.getAllNonAdminAccounts(account.getAccountID());
            // Set thymeleaf models ...
            model.addAttribute("otherAccounts", otherAccounts);
            model.addAttribute("account", account);
            model.addAttribute("accountType", account.getAccountType().name());  // Add accountType as a string
            model.addAttribute("accountTypes", Account.AccountType.values());  // Add enum values for dropdown
            // set view ...
            return "admAccountList";
        } else {

            // If a visitor is not admin, send them home ...
            model.addAttribute("account", account);
            return "redirect:/";
        }
    }

    @GetMapping(value = {"accounts/add","accounts/add/" })
    public String addAccount(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        if (account != null && account.getAccountType() == Account.AccountType.ADMIN) {
            model.addAttribute("account", account);
            model.addAttribute("accountTypes", Account.AccountType.values());  // Add enum values for dropdown
            return "admAddAccount";
        }
        else {
            return "redirect:/";
        }
    }

    @PostMapping("/accounts/add")
    public String addAccount(@RequestParam("accountName") String accountName,
                             @RequestParam("accountPassword") String accountPassword,
                             @RequestParam("accountType") Account.AccountType accountType) {
        // We send the parameters tp the service layer and then to the repository layer for Account ...
        accountService.addAccount(accountName, accountPassword, accountType);
        return "redirect:/adm/accounts/list/";
    }

    @PostMapping(value = {"/accounts/list/edit", "/accounts/list/edit/"})
    public String admEditAccount(@RequestParam("accountId") int accountID,
                                 @RequestParam Map<String, String> allParams, // Capture all parameters
                                 HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");

        if (account != null && account.getAccountType() == Account.AccountType.ADMIN) {
            // Extract the correct newAccountType using the dynamic parameter name
            String newAccountTypeParam = allParams.get("newAccountType_" + accountID);
            // Parse the enum value from the string
            Account.AccountType newAccountType = Account.AccountType.valueOf(newAccountTypeParam);
            // Call the service to update the account type
            accountService.updateAccountType(accountID, newAccountType);
            // Redirect to accounts page after the update
            return "redirect:/adm/accounts/list";
        } else {
            model.addAttribute("account", account);
            return "redirect:/";
        }
    }
}
