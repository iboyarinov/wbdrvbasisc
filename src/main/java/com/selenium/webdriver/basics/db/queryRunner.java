package com.selenium.webdriver.basics.db;

import java.sql.*;

/**
 * Created by Hedg on 11.02.14.
 */
public class queryRunner {

    public static ResultSet runQuery(Statement st, String query) throws SQLException {
        ResultSet rs = st.executeQuery(query);
        return rs;
    }

}
