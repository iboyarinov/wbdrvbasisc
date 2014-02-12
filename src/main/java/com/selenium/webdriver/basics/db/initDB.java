package com.selenium.webdriver.basics.db;

/**
 * Created by Hedg on 11.02.14.
 */

import com.selenium.webdriver.basics.Config;

import java.sql.*;

public class initDB {

    private static Connection createConnection() throws Exception {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite::memory:");

        } catch (SQLException e) {
            System.out.println("SQLException" + e.getMessage());
        }
        return connection;
    }

    public static void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }


    public static void createDB() throws Exception {

        Statement statement = createConnection().createStatement();

        try {
            statement.executeUpdate(Config.getProperty("brandsData.sql"));
        //    statement.executeUpdate(Config.getProperty("products"));
     //       statement.executeUpdate(Config.getProperty("product_variants"));

     //       statement.executeUpdate(Config.getProperty(""));
       //     statement.executeUpdate(Config.getProperty(""));
      //      statement.executeUpdate(Config.getProperty(""));
        } catch (SQLException e) {
            System.out.println("Cannot create DB " + e.getSQLState() + e.getMessage());
        }
    }

}
