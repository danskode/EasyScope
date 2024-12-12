package org.kea.easyscope.controller;

import jakarta.servlet.http.HttpSession;
import org.kea.easyscope.model.Account;
import org.kea.easyscope.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("")
public class SessionController {

    private final AccountService accountService;
    // Controller injection ...
    public SessionController(AccountService accountService) {
        this.accountService = accountService;
    }

    // This is our main page (home.html), would be EasyScope.io in real life but for now :8080 ...
    @GetMapping()
    public String home(HttpSession session, Model model) {
        // Make an account object from the session ...
        Account account = (Account) session.getAttribute("account");
        // Make ready to use Thymeleaf in home.html ...
        model.addAttribute("account", account);
        // Send the visitor back to home.html ....
        return "home";
    }

    // First we start a session and add an account to it ...
    @GetMapping(value = {"/login", "/login/"})
    public String showLogin(){
        return "redirect:/";
    }

    // Then we add the postmapping method to manage the input from the form on home.html ...
    @PostMapping("/login")
    public String loginLogic(@RequestParam String accountName, @RequestParam String accountPassword, HttpSession session, Model model, RedirectAttributes redirectAttributes){
        // First we find an account in our table ...
        Account account = accountService.getAccountFromAccountName(accountName);
        // And then we return it as an account object to be checked against null, name and password provided by visitor ...
        if(account != null && account.getAccountPassword().equals(accountPassword)){
            // We then put an object for the session (without password of cause) ...
            session.setAttribute("account", account);
            // And we send the identified visitor to their account page (account.html) ...
            return "redirect:";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid account or password");
            //model.addAttribute("error", "Invalid account or password");
            // The visitor failed to provide the right credentials, so they have to try again. Back to start ... or almost ... back to home.html ...
            return "redirect:/";
        }
    }

    // And finally we want to be able to end the session / invalidate it ...
    @GetMapping(value = {"/logout", "/logout/"})
    public String endSession(HttpSession session) {
        session.invalidate();
        // The identified visitor chose to end the session, so we send them back to the main page to start over or just to go for a coffee breake ...
        return "redirect:";
    }
}
