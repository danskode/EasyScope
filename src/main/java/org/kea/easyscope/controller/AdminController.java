package org.kea.easyscope.controller;

import jakarta.servlet.http.HttpSession;
import org.kea.easyscope.model.Account;
import org.kea.easyscope.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/adm")
public class AdminController {

    private final AccountService accountService;

    public AdminController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = {"/accounts/list", "/accounts/list/"})
    public String adminAccounts(HttpSession session,
                                Model model) {
        // Retrieve the account from session
        Account account = (Account) session.getAttribute("account");

        // Check if account is not null and has ADMIN privileges
        if (account == null) {
            // If no account is found in session, throw an exception
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }

        if (account.getAccountType() != Account.AccountType.ADMIN) {
            // If the account is not admin, throw an exception
            throw new IllegalArgumentException("You don't have permission to view this page.");
        }

        // Get a list of all other accounts which are not admins
        List<Account> otherAccounts = accountService.getAllNonAdminAccounts(account.getAccountID());

        // Set models for Thymeleaf
        model.addAttribute("otherAccounts", otherAccounts);
        model.addAttribute("account", account);

        return "admAccountList";
    }


    @GetMapping(value = {"/accounts/edit/{id}", "/accounts/edit/{id}"})
    public String adminAccounts(HttpSession session,
                                Model model,
                                @PathVariable int id) {
        Account account = (Account) session.getAttribute("account");
        Account otherAccount = accountService.getAccountFromID(id);

        // does the account exist?
        if (account == null) {
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }

        // is the account an admin?
        if (account.getAccountType() != Account.AccountType.ADMIN) {
            throw new IllegalArgumentException("You don't have permission to edit accounts.");
        }

        // if account is not found
        if (otherAccount == null) {
            throw new IllegalArgumentException("The account you are trying to edit does not exist.");
        }

        if (otherAccount.getAccountType() == Account.AccountType.PROJECT_MANAGER) {
            // Get a list of project managers
            List<Account> projectManagers = accountService.getAllProjectManagers(account.getAccountID());
            model.addAttribute("otherAccount", otherAccount); // the account we want to edit ...
            model.addAttribute("account", account); // the logged in admin ...
            model.addAttribute("projectManagers", projectManagers); // to make a list of other project managers to give projects to ...
            model.addAttribute("accountType", account.getAccountType().name()); // Add accountType as a string to edit account type
            model.addAttribute("accountTypes", Account.AccountType.values()); // Add enum values for dropdown
            // set view ...
            return "admAccountEdit";
        } else if (otherAccount.getAccountType() == Account.AccountType.TEAM_MEMBER) {
            // Get a list of team members
            List<Account> teamMembers = accountService.getAllTeamMembers(otherAccount.getAccountID());
            model.addAttribute("otherAccount", otherAccount); // the account we want to edit ...
            model.addAttribute("account", account); // the logged in admin ...
            model.addAttribute("teamMembers", teamMembers); // to make a list of other team members to give projects to ...
            model.addAttribute("accountType", account.getAccountType().name()); // Add accountType as a string to edit account ype
            model.addAttribute("accountTypes", Account.AccountType.values()); // Add enum values for dropdown
            // set view ...
            return "admAccountEdit";
        } else {
            throw new IllegalArgumentException("The account cannot be edited");
        }
    }


    @GetMapping(value = {"accounts/add", "accounts/add/"})
    public String addAccount(HttpSession session,
                             Model model) {
        Account account = (Account) session.getAttribute("account");

        // Check if there is an account in the session and if the account has ADMIN privileges
        if (account != null) {
            if (account.getAccountType() == Account.AccountType.ADMIN) {
                model.addAttribute("account", account);
                model.addAttribute("accountTypes", Account.AccountType.values());  // Add enum values for dropdown
                return "admAddAccount";
            } else {
                // If the account does not have the necessary permissions, throw an IllegalArgumentException
                throw new IllegalArgumentException("You do not have the necessary permissions to add an account.");
            }
        } else {
            // If there is no account in the session, throw an IllegalArgumentException
            throw new IllegalArgumentException("No account found in session. Please log in.");
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

    // Edit a project manager's account ...
    @PostMapping("/accounts/edit/projectmanager")
    public String editProjectManager(
            @RequestParam("accountId") int accountId,
            @RequestParam("newProjectManagerID") int newProjectManagerID,
            @RequestParam("newAccountType") Account.AccountType newAccountType) {

        accountService.updateProjectManager(accountId, newAccountType, newProjectManagerID);
        return "redirect:/adm/accounts/list";
    }

    // Edit a team member's account ...
    @PostMapping("/accounts/edit/teammember")
    public String editTeamMember(
            @RequestParam("accountId") int accountId,
            @RequestParam("newAccountType") Account.AccountType newAccountType,
            @RequestParam("newTeamMemberID") int newTeamMemberID) {

        accountService.updateTeamMember(accountId, newAccountType, newTeamMemberID);
        return "redirect:/adm/accounts/list";
    }
}
