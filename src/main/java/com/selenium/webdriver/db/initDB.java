package com.selenium.webdriver.db;

/**
 * Created by Hedg on 11.02.14.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class initDB {

    public static void createConnection() throws Exception {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:com.webdriver.mystest.sqlite:mock.db");
            Statement st = connection.createStatement();
            st.setQueryTimeout(60);



        } catch (SQLException e) {
            System.out.println("SQLException" + e.getMessage());
        }
    }

    private static boolean ifTableExist(Statement statement, String tableName) throws SQLException {
        try {
            statement.execute("SELECT * FROM " + tableName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

}
