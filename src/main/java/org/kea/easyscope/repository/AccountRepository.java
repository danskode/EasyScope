package org.kea.easyscope.repository;

import org.kea.easyscope.model.Account;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Account getAccountFromID(int id) {

        String sql = "SELECT account_id, account_name, account_type FROM accounts WHERE account_id=?";

        RowMapper<Account> rowMapper = new RowMapper<>() {

            public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
                int accountID = rs.getInt("account_id");
                String accountName = rs.getString("account_name");
                String accountTypeString = rs.getString("account_type");

                // Print values to verify
                System.out.println("accountID: " + accountID + " (Type: " + ((Object) accountID).getClass().getName() + ")");
                System.out.println("accountName: " + accountName + " (Type: " + accountName.getClass().getName() + ")");
                System.out.println("accountTypeString: " + accountTypeString + " (Type: " + accountTypeString.getClass().getName() + ")");

                Account.AccountType accountType = Account.AccountType.valueOf(accountTypeString);
                return new Account(accountID, accountName, accountType);
            }
        };
        try {
            Account account;
            account = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return account;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Account getAccountFromAccountName(String accountName) {
        String sql = "SELECT account_id, account_name, account_type, account_password FROM accounts WHERE account_name=?";
        RowMapper<Account> rowMapper = new RowMapper<>() {
            public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
                int accountID = rs.getInt("account_id");
                String accountName = rs.getString("account_name");
                String accountTypeString = rs.getString("account_type");
                String accountPassword = rs.getString("account_password");

                Account.AccountType accountType = Account.AccountType.valueOf(accountTypeString);
                return new Account(accountID, accountName, accountPassword, accountType);
            }
        };
        try {
            Account account;
            account = jdbcTemplate.queryForObject(sql, rowMapper, accountName);
            return account;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
