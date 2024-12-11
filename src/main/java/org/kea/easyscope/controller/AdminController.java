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
    public String adminAccounts(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");

        if (account != null && account.getAccountType() == Account.AccountType.ADMIN) {
            // Get a list of all other accounts which are not admins ...
            List<Account> otherAccounts = accountService.getAllNonAdminAccounts(account.getAccountID());

            // Set thymeleaf models ...
            model.addAttribute("otherAccounts", otherAccounts);
            model.addAttribute("account", account);

            return "admAccountList";
        } else {
            // If a visitor is not admin, send them home ...
            model.addAttribute("account", account);
            return "redirect:/";
        }
    }

    @GetMapping(value = {"/accounts/edit/{id}", "/accounts/edit/{id}"})
    public String adminAccounts(HttpSession session, Model model, @PathVariable int id) {
        Account account = (Account) session.getAttribute("account");
        Account otherAccount = accountService.getAccountFromID(id);

        if (account != null && account.getAccountType() == Account.AccountType.ADMIN) {

            if (otherAccount != null && otherAccount.getAccountType() == Account.AccountType.PROJECT_MANAGER) {
                // Get a list of project managers ..
                List<Account> projectManagers = accountService.getAllProjectManagers(account.getAccountID());
                model.addAttribute("otherAccount", otherAccount); // the account we want to edit ...
                model.addAttribute("account", account); // the logged in admin ...
                model.addAttribute("projectManagers", projectManagers); // to make a list of other project managers to give projects to ...
                model.addAttribute("accountType", account.getAccountType().name());  // Add accountType as a string to edit accounttype
                model.addAttribute("accountTypes", Account.AccountType.values());  // Add enum values for dropdown
                // set view ...
                return "admAccountEdit";
            } else if (otherAccount != null && otherAccount.getAccountType() == Account.AccountType.TEAM_MEMBER) {
                // Get a list of team members ..
                List<Account> teamMembers = accountService.getAllTeamMembers(otherAccount.getAccountID());
                model.addAttribute("otherAccount", otherAccount); // the account we want to edit ...
                model.addAttribute("account", account); // the logged in admin ...
                model.addAttribute("teamMembers", teamMembers); // to make a list of other team members to give projects to ...
                model.addAttribute("accountType", account.getAccountType().name());  // Add accountType as a string to edit accounttype
                model.addAttribute("accountTypes", Account.AccountType.values());  // Add enum values for dropdown
                // set view ...
                return "admAccountEdit";
            } else {
                return "redirect:/";
            }
        }
        // If a visitor is not admin, send them home ...
        return "redirect:/";
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

    // Edit a project manager's account ...
    @PostMapping("/accounts/edit/projectmanager")
    public String editProjectManager(
            @RequestParam("accountId") int accountId,
            @RequestParam("newProjectManagerID") int newProjectManagerID,
            @RequestParam("newAccountType") Account.AccountType newAccountType,
            Model model
    ) {
        accountService.updateProjectManager(accountId, newAccountType, newProjectManagerID);
        return "redirect:/adm/accounts/list";
    }

    // Edit a team member's account ...
    @PostMapping("/accounts/edit/teammember")
    public String editTeamMember(
            @RequestParam("accountId") int accountId,
            @RequestParam("newAccountType") Account.AccountType newAccountType,
            @RequestParam("newTeamMemberID") int newTeamMemberID,
            Model model
    ) {
        accountService.updateTeamMember(accountId, newAccountType, newTeamMemberID);
        return "redirect:/adm/accounts/list";
    }









//    // Edit other non-admin users' accountType ...
//    @PostMapping(value = {"/accounts/list/edit", "/accounts/list/edit/"})
//    public String admEditAccount(@RequestParam("accountId") int accountID,
//                                 //@RequestParam("newProjectManagerID") int newProjectManagerID,
//                                 @RequestParam Map<String, String> allParams, // Capture all parameters
//                                 HttpSession session, Model model) {
//        Account account = (Account) session.getAttribute("account");
//
//        if (account != null && account.getAccountType() == Account.AccountType.ADMIN) {
//            // Extract the correct newAccountType using the dynamic parameter name
//            String newAccountTypeParam = allParams.get("newAccountType_" + accountID);
//            // Get the newProjectManagerID ...
//            //int newProjectManagerID =  allParams.get("newProjectManagerID_" + (int) newProjectManagerID);
//            // Parse the enum value from the string
//            Account.AccountType newAccountType = Account.AccountType.valueOf(newAccountTypeParam);
//            // Make a int out of the end of param ...
//            //int newProjectManagerID = Integer.parseInt(newProjectManagerParam);
//            // Call the service to update the account type
//            accountService.updateAccountType(accountID, newAccountType, newProjectManagerID);
//            // Redirect to accounts page after the update
//            return "redirect:/adm/accounts/list";
//        } else {
//            model.addAttribute("account", account);
//            return "redirect:/";
//        }
//    }

    // Getmapping for edit
    // Postmapping for edit
}
