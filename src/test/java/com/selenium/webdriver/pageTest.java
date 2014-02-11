package com.selenium.webdriver;

import com.selenium.webdriver.basics.Driver;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by Hedg on 11.02.14.
 */
public class pageTest {

    WebDriver wb;

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
    }

    @Test
    public void runTest() throws Exception {
        try {
        } catch (Exception e) {

        }
    }
}