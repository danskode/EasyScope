package org.kea.easyscope.model;

public class Account {

    int accountID;
    private String accountName;
    private String accountPassword;
    private AccountType accountType;

    // All parameters constructor, except id which is auto incremented in DB ...
    public Account(String accountName, String accountPassword, AccountType accountType) {
        this.accountName = accountName;
        this.accountPassword = accountPassword;
        this.accountType = accountType;
    }

    // Constructor for Accounts without password, might be useful in HttpSession? ...
    public Account(int id, String accountName, AccountType accountType) {
        this.accountID = id;
        this.accountName = accountName;
        this.accountType = accountType;
    }

    // Nested enum accountType, simplest solution for this project ...
    public enum AccountType {
        ADMIN("admin"),
        PROJECT_MANAGER("project manager"),
        TEAM_MEMBER("team member");

        // String to print enum pretty ...
        private final String accountTypeName;

        // Constructor for Enum with string parameter ...
        AccountType(String accountType) {
            this.accountTypeName = accountType;
        }

        // Getter for Enum to display as a name ...
        public String getAccountTypeName() {
            return accountTypeName;
        }
    }

    @Override
    public String toString() {
        return getAccountName() + " " + accountType.getAccountTypeName();
    }

    // All getters and setters ...
    public int getAccountID() {
        return accountID;
    }
    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }
    public String getAccountName() {
        return accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public String getAccountPassword() {
        return accountPassword;
    }
    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }
    public Enum getAccountType() {
        return accountType;
    }
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
