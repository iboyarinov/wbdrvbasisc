package com.selenium.webdriver.basics;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by Hedg on 11.02.14.
 */
public class Driver {

    public static WebDriver getDriver() throws Exception {
        WebDriver wb = null;
        try {
            wb = new FirefoxDriver();
        } catch (Exception e) {
            System.out.println("Cannot create webdriver " + e.getMessage());
        }

        return wb;
    }
}