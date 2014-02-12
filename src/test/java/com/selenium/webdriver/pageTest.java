package com.selenium.webdriver;

import com.selenium.webdriver.basics.Config;
import com.selenium.webdriver.basics.Driver;
import com.selenium.webdriver.basics.db.initDB;
import com.selenium.webdriver.basics.testng.TestDataProvider;
import com.selenium.webdriver.basics.testng.TestDataSource;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Hedg on 11.02.14.
 */
public class pageTest {

    WebDriver wb;
    Connection conn;

    @BeforeTest
    public void clearCash() throws Exception {

    }

    @BeforeSuite
    public void setUp() throws Exception {
        try {
            wb = Driver.getDriver();
        } catch (Exception e) {
            System.out.println("Couldn't start WebDriver " + e.getMessage());
        }

        //init db
        try {
            conn = initDB.createConnection();
        } catch (SQLException e) {
                  System.out.println("Cannot get instance of DB "+ e.getMessage());
        }
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "csv2HashDataProvider")
    @TestDataSource(csv = "CSVData/SimpleTest.csv", csvDelimiter = ";")
    public void runTest(HashMap<String, String> testData) throws Exception {


        try {
        } catch (Exception e) {

        }
    }
}