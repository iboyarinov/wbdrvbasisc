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
        //    System.out.print(statement.execute("select * from brands"));
        //    statement.executeUpdate("drop table if exists PRODUCT_VARIANTS;");
        //    statement.executeUpdate("drop table if exists PRODUCTS;");
        //    statement.executeUpdate("drop table if exists BRANDS;");
  //          statement.executeUpdate(loadSQLfromFile(Config.getProperty("brandsTableStructure")));

            //insert data to table not perfect, but quickly
     //       statement.executeUpdate("insert into brands values(1,\"\");");
     //       statement.executeUpdate("");

     //      statement.executeUpdate(loadSQLfromFile(Config.getProperty("productsTableStructure")));
   //         statement.executeUpdate(loadSQLfromFile(Config.getProperty("productsData")));
            //       statement.executeUpdate(Config.getProperty("product_variants"));

            //       statement.executeUpdate(Config.getProperty(""));
            //     statement.executeUpdate(Config.getProperty(""));
            //      statement.executeUpdate(Config.getProperty(""));
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
