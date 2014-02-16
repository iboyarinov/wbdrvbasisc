package com.selenium.webdriver.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.selenium.webdriver.basics.Config;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


/**
 * Created by Hedg on 13.02.14.
 */
public class product {

    public static void clickOnProductByName(String name) {

        $(By.xpath("//a[text() = '" + name + "']")).click();

    }

    public static List<String> getProductsFromPage(String xPathProducts) {

        List products = $$(By.xpath(xPathProducts));

        return products;
    }

    public static Map<String, Object> getPrices(String xPathPrice, String xPathOldPrice) {
        Map<String, Object> prices = new HashMap<>();
        String reduce_prc = null;
        try {
            //wait for price update after product type selection
            //it's a little slow down test
            $(By.xpath(Config.getProperty("by_xPathReducedLabel"))).shouldBe(Condition.visible);
            //remove currency from price
            String prc = $(By.xpath(xPathPrice)).getText();
            prc = prc.substring(0, prc.indexOf(" ")).replace(",", ".");
//remove currency from old_price
            $(By.xpath(xPathOldPrice)).exists();
            reduce_prc = $(By.xpath(xPathOldPrice)).getText();
            String reduce_prc1 = reduce_prc.substring(0, reduce_prc.indexOf(" ")).replace(",", ".");

            prices.put("Price", Double.parseDouble(prc));
            prices.put("OldPrice", Double.parseDouble(reduce_prc1));
        } catch (Exception e) {
            System.out.println("Cannot get price " + reduce_prc + " " + e.getMessage());
        }
        return prices;
    }

    public static Double getPrice(String xPath) {


        //   $(By.xpath("//div[contains(@class,'discount_labe')]")).shouldBe(Condition.visible);
        //remove currency from price
        String prc = $(By.xpath(xPath)).getText();
        //remove substring ab and currency
        prc = prc.substring((prc.indexOf(" "))).trim();
        prc = prc.substring(0, prc.indexOf(" ")).replace(",", ".");
        return Double.parseDouble(prc);
    }

    public static Double getOldPrice(String xPath) {
        //remove currency from price
        String reduce_prc = $(By.xpath("//*[contains(@class,'_previous_price')]")).getText();
        reduce_prc = reduce_prc.substring(0, reduce_prc.indexOf(" ")).replace(",", ".");

        return Double.parseDouble(reduce_prc);
    }

    public static Map<String, String> getArtNrByXpath(String xPathTitle, String xPathValue) {
        $(By.xpath(xPathTitle)).should(Condition.appear);
        String[] artNum = ($(By.xpath(xPathValue)).getText()).split("\n");
        Map<String, String> value = new HashMap<>();
        value.put(artNum[0], artNum[1]);

        return value;
    }

    public static Map<String, String> getEANByXpath(String xPathTitle, String xPathValue) {
        $(By.xpath(xPathTitle)).should(Condition.appear);
        String[] ean = $(By.xpath(xPathValue)).getText().split("\n");
        Map<String, String> value = new HashMap<>();
        value.put(ean[0], ean[1]);
        return value;
    }

    public static String getManufacturerByCSS(String cssSelector) {
        $(By.cssSelector(cssSelector));
        String manufacturer = $(By.cssSelector(cssSelector)).getText();

        return manufacturer;
    }

    public static String isAvailableByXpath(String xPath) {
        String result = null;
        if ($(By.xpath(xPath)).getText().equals("Sofort lieferbar")) {
            result = "Y";
        }

        return result;
    }

    public static Integer getStockByXpath(String xPath) {

        String value = $(By.xpath(xPath)).getText();
        //cat only number part from page
        value = value.substring(0, value.indexOf(" "));

        return Integer.parseInt(value);
    }

    public static Integer getPointsAmountByXpath(String xPath) {

        return Integer.parseInt($(By.xpath(xPath)).getText());

    }

    public static void putToCart() {
        $(By.name("button")).isDisplayed();
        $(By.name("button")).click();
    }

    public static Double reducePercent(String xPath) {
        String prc = $(By.xpath(xPath)).getText();
        prc = prc.replace(" ", "");
        prc = prc.substring(0, prc.indexOf("%"));

        return Double.parseDouble(prc);
    }

}
