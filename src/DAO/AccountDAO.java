package DAO;

import java.sql.*;
import java.util.*;

import entity.Account;

public class AccountDAO {
    private static AccountDAO instance = new AccountDAO();

    public static AccountDAO getInstance() {
        if (instance == null)
            instance = new AccountDAO();
        return instance;
    }

    public ArrayList<Account> getListAccount() {
        String query = "SELECT * FROM dbo.Account";
        ResultSet rs = DataProvider.getInstance().ExecuteQuery(query, null);
        ArrayList<Account> dataList = new ArrayList<Account>();
        try {
            while (rs.next()) {
                dataList.add(new Account(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public boolean Login(String username, String password) {
        int count = 0;
        String query = "{CALL USP_Login ( ? , ? )}";
        Object[] parameter = new Object[] { username, password };
        ResultSet rs = DataProvider.getInstance().ExecuteQuery(query, parameter);
        try {
            rs.next();
            count = rs.getRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count > 0;
    }

    public Account getAccountByUsername(String username) {
        String query = "Select * from dbo.Account where username = ?";
        Object[] parameter = new Object[] { username };
        ResultSet rs = DataProvider.getInstance().ExecuteQuery(query, parameter);
        Account account = null;
        try {
            rs.next();
            account = new Account(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
}
