package com.selenium.webdriver.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by Hedg on 13.02.14.
 */
public class product {

    public static void clickOnProductByName(WebDriver wb, String name) {


        WebElement product = wb.findElement(By.xpath("//a[text() = '" + name + "']"));
        product.click();
    }

    public static Double getPrice(WebDriver wb, String priceName) {
        Double price = 0.0;

        if (priceName == "actualPrice") {
            wb.findElement(By.xpath("//span[class='price']")).getTagName();
        } else {

        }

        return price;
    }

}
