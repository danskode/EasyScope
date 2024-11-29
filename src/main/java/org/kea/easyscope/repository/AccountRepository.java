package org.kea.easyscope.repository;

import org.kea.easyscope.model.Account;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Account> getAllNonAdminAccounts(int accountID) {

        String sql = "SELECT account_id, account_name, account_type FROM accounts WHERE account_id != ? AND account_type != 'ADMIN'";
        RowMapper<Account> rowMapper = new RowMapper<>() {
            public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
                int accountID = rs.getInt("account_id");
                String accountName = rs.getString("account_name");
                String accountTypeString = rs.getString("account_type");

                Account.AccountType accountType = Account.AccountType.valueOf(accountTypeString);
                return new Account(accountID, accountName, accountType);
            }
        };
        try {
            // Adds the account objects to a list automatically ...
            return jdbcTemplate.query(sql, rowMapper, accountID);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>(); // Just an empty ArrayList ...
        }
    }

    // Admin specific ...
    public void updateAccountType(int accountID, Account.AccountType newAccountType) {
        String sql = "UPDATE accounts SET account_type=? WHERE account_id=?";
        jdbcTemplate.update(sql, newAccountType.name(), accountID);
    }
}
