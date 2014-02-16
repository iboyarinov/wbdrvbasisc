package com.selenium.webdriver.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by Hedg on 12.02.14.
 */
public class filters {

    public static void resetFilterByXpath(String xPath) {
        $(By.xpath(xPath)).isDisplayed();
        $(By.xpath(xPath)).click();
        $(By.xpath(xPath + "/div//li[@class='active']//a")).click();

    }

    public static List<String> getItemFromFilter(String filterXpath, String attributesXpath) {
        List<String> results = new ArrayList();
        try {
            $(By.xpath(filterXpath)).click();
            List<SelenideElement> elements = $$(By.xpath(attributesXpath));
            for (SelenideElement element : elements) {

                //skip Alle Marken because is not a brand
                if (!element.getText().equals("Alle Marken"))
                    results.add(element.getText());
                {
                }
            }

        } catch (Exception e) {
            System.out.println("Cannot find filter " + " " + e.getMessage());
        }
        return results;
    }


    public static void setListBoxFilter(String filterXpath, String value) {

        try {
            $(By.xpath(filterXpath)).click();
            $(By.xpath("//*[contains(text(),'" + value + "')]")).click();
        } catch (Exception e) {

            System.out.println("Cannot set filter " + value + e.getMessage());
        }

    }

    public static String checkFilterValue(String filterXpath) {
        String checkedElements = null;

        try {
            $(By.xpath(filterXpath)).isDisplayed();
            $(By.xpath(filterXpath)).click();
            checkedElements = $(By.xpath(filterXpath + "/div//li[@class='active']//a")).getText();
        } catch (Exception e) {
            System.out.println("Cannot get selected item from combobox " + e.getMessage());
        }
        return checkedElements;
    }

    public static List<String> getAllProductTypesByXpath(String Xpath) {

        List result = new ArrayList(Arrays.asList($$(By.xpath(Xpath)).getTexts()));

        return result;
    }

    public static List<String> getVariantsById(String id) {

        List<String> list = new ArrayList<String>(Arrays.asList($(By.id(id)).getText().split("\n")));
        //remove - Auswählen - as not needed value
        list.remove(0);

        return list;
    }

    public static void setVariantTypeById(String id, String value) {
        //      $(By.id(id)).click();
        $(By.id(id)).sendKeys(value);

    }

    public static List<String> getVariantsOptionsById(String id, String optionType) throws Exception {
        String key = null;
        List<String> result = new ArrayList<>();


        $(By.id(id)).click();
        //need to wait while lisboxes synchronized
        Thread.sleep(1500);
        List<SelenideElement> elements = $(By.id(id)).$$(By.tagName("option"));

        switch (optionType) {
            case "black":
                key = "rakuten_product_control_variant_value_black";
                break;
            case "grey":
                key = "rakuten_product_control_variant_value_grey";
                break;
        }

        for (SelenideElement element : elements) {
            if (key.equals(element.getAttribute("class"))) {
                result.add(element.getText());
            }
        }

        return result;
    }
}
