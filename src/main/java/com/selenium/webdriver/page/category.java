package com.selenium.webdriver.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by Hedg on 12.02.14.
 */
public class category {

    public static void navigateToCategoryByLinkText(WebDriver wb, String categoryName) {
        try {
            wb.findElement(By.linkText(categoryName)).click();

        } catch (Exception e) {
            System.out.println("Cannot find category " + e.getMessage());
        }
    }
}
