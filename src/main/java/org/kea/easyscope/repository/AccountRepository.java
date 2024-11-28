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

    public Account getAccountFromID() {
        String sql = "SELECT account_id, account_name, account_type FROM account WHERE id = ?";

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
            // Attempt to retrieve the account
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            // No account found for the provided id, return null or throw custom exception
            return null;
        }
    }
}
