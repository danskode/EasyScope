package org.kea.easyscope;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import org.kea.easyscope.model.Account;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.kea.easyscope.model.Account.AccountType.*;

@SpringBootApplication
public class EasyScopeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyScopeApplication.class, args);

        Account test = new Account("Mr. Test", "test", ADMIN);
        String username = test.getAccountName();
        Enum accountType = test.getAccountType();
        Account loggedIn =  new Account("Jens", "kodeord", TEAM_MEMBER);
        System.out.println("Bruger, der er logget ind: " + loggedIn + " " + "Bruger, der også er i systemet: " + test +  " " + accountType);

    }
}
