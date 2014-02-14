package com.selenium.webdriver.page;

import com.codeborne.selenide.SelenideElement;
import com.selenium.webdriver.basics.Config;
import org.apache.xml.serializer.utils.SerializerMessages_ru;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by Hedg on 12.02.14.
 */
public class filters {

    public static void resetFilterByName(String filterName) {

    }


    // public static List<String> getAllValuesFromFilterByClass(WebDriver webDriver){

//  }


    public static void resetFilter(String name) {

    }

    public static List<String> getItemFromFilter(WebDriver wb, String filterXpath, String attributesXpath) {
        List<String> results = new ArrayList();
        try {
            $(By.xpath(filterXpath)).click();
            List<SelenideElement> elements = $$(By.xpath(attributesXpath));
            for (SelenideElement element : elements) {
                results.add(element.getText());
            }

        } catch (Exception e) {
            System.out.println("Cannot find filter " + " " + e.getMessage());
        }
        return results;
    }

    public static List<String> getSelectedItemFromFilter(WebDriver wb, String filterName, String selectedAttributeXpath) {
        List<String> selected = new ArrayList();

        return selected;
    }

    public static void setListBoxFilter(WebDriver webDriver, String filterXpath, String value) {

        try {
            $(By.xpath(filterXpath)).click();
            $(By.xpath("//*[contains(text(),'" + value + "')]")).click();
        } catch (Exception e) {

            System.out.println("Cannot set filter " + value + e.getMessage());
        }

    }

    public static List<String> getListBoxFilterValues(WebDriver wb, String filterName) {
        List<String> values = new ArrayList<String>();

        return values;
    }


}
