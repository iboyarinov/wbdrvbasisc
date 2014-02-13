package com.selenium.webdriver.basics.db;

/**
 * Created by Hedg on 11.02.14.
 */

import com.selenium.webdriver.basics.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class initDB {

    private static Connection createConnection() throws Exception {
        Connection connection = null;

        try {
         //   connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        connection = DriverManager.getConnection("jdbc:sqlite::resource:mock.db");

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
            statement.executeQuery("select * from brands");
        } catch (SQLException e) {
            System.out.println("Cannot create DB " + e.getSQLState() + e.getMessage());
        }
    }

    private static String loadSQLfromFile(String filePath) throws IOException {
        String queryStr = null;

        try {
            StringBuffer fileData = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(initDB.class.getClassLoader().getResourceAsStream(filePath)));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                fileData.append(readData);
            }
            reader.close();
            queryStr = fileData.toString();

        } catch (Exception e) {
            System.out.println("Cannot read file with SQL query " +e.getMessage());
        }

        return queryStr;
    }

}
