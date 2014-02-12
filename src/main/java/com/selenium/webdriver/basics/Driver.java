package com.selenium.webdriver.basics;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

/**
 * Created by Hedg on 11.02.14.
 */
public class Driver {

    public static WebDriver getDriver() throws Exception {
        WebDriver wb = null;
        try {
            FirefoxProfile profile = new FirefoxProfile();
            wb = new FirefoxDriver(profile);
        } catch (Exception e) {
            System.out.println("Cannot create webdriver " + e.getMessage());
        }

        return wb;
    }
}