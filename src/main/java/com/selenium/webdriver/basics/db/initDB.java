package com.selenium.webdriver.basics.db;

/**
 * Created by Hedg on 11.02.14.
 */

import com.selenium.webdriver.basics.Config;

import java.sql.*;

public class initDB {

    public static Connection createConnection() throws Exception {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:com.webdriver.mystest.sqlite:mock.db");
            Statement st = connection.createStatement();
            st.setQueryTimeout(60);

            //check if tables already exist
            String[] tables = Config.getProperty("tables").split(":");
            for (String str : tables) {
                if (!ifTableExist(st, str)) {
                    ResultSet rs = queryRunner.runQuery(st, Config.getProperty(str));
                }
            }


        } catch (SQLException e) {
            System.out.println("SQLException" + e.getMessage());
        }
        return connection;
    }

    private static boolean ifTableExist(Statement statement, String tableName) throws SQLException, Exception {

        try {
            statement.execute("SELECT * FROM " + tableName);
            return true;
        } catch (SQLException e) {
            return false;

        }
    }

    private static void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }

}
