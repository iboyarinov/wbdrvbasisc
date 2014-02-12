package com.selenium.webdriver.basics.db;

import java.sql.*;

/**
 * Created by Hedg on 11.02.14.
 */
public class queryRunner {

    public static ResultSet runQuery(Connection connection, String query) throws SQLException {

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        return rs;
    }

}
