package com.selenium.webdriver;

import com.codeborne.selenide.Condition;
import com.selenium.webdriver.basics.Config;
import com.selenium.webdriver.basics.db.initDB;
import com.selenium.webdriver.basics.testng.TestDataProvider;
import com.selenium.webdriver.basics.testng.TestDataSource;
import com.selenium.webdriver.page.filters;
import com.selenium.webdriver.page.product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.filter;

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
           /* wb = getWebDriver();
            wb.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);*/
        } catch (Exception e) {
            System.out.println("Couldn't start WebDriver " + e.getMessage());
        }

        //init db
        try {
            initDB.createDB();
        } catch (SQLException e) {
            System.out.println("Cannot get instance of DB " + e.getMessage());
        }
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "csv2HashDataProvider")
    @TestDataSource(csv = "/CSVData/SimpleTest.csv", csvDelimiter = ";")
    public void runTest(HashMap<String, String> testData) throws Exception {
        String[] startURL = testData.get("startURL").split(";");

        open(startURL[0]);
        $(By.linkText("Teste")).click();

        url();

        List<String> items = filters.getItemFromFilter(Config.getProperty("by_xPath.FilterBrands"), Config.getProperty("by_xPath.FilterBrandsElements"));
        System.out.println("Size is " + items.size());


        $(By.linkText("Timberland")).click();


        product.getProductsFromPage();
        product.clickOnProductByName(wb, "Testing umlauts - öäz23gf");


        //find how many variants available
  //      WebElement var = wb.findElement(By.xpath("//div[@class='variants']"));
        //  System.out.println(var.getText());

        //find first listbox
        //       WebElement las = var.findElement(By.xpath("//*[@class='label' and contains(text(),'Variant type')]"));
        //       System.out.println("Single element " + las.getText());
  //      List<WebElement> variants = var.findElements(By.xpath("//*[@class='label' and contains(text(),'Variant type')]"));

        //catch all elements
   /*     for (WebElement varType : variants) {

            // List<WebElement> types = varType.findElements(By.xpath("/*//*[@id='rakuten_control_variant_1']"));


            System.out.println(varType.getText());

        }*/
        //get all available product types
        //6.    Notice that the product has 3 variant types: Variant type 1, Variant type 2 and Variant type 3 (Check against DB)
        filters.getAllProductTypesByXpath(Config.getProperty("by_xPathProductVariants"));

        //get values from listbox 1
        //7.    For variant type ‘Variant type 1’ check that there are 3 variant values: Variant value 1, Variant value 11 and Variant value 111 (Check against DB)
        List<String> vars = filters.getVariantsById(Config.getProperty("by_Id.VariantBox1"));

        //set variant to selected
        filters.setVariantTypeById(Config.getProperty("by_Id.VariantBox1"), "Variant value 1");

//work wariant
      /*  Select list1 = new Select(wb.findElement(By.id("rakuten_control_variant_1")));
        list1.selectByVisibleText("Variant value 1");
*/
/*
        WebElement listBox = wb.findElement(By.xpath("/*/
/*[@id='rakuten_control_variant_1']"));
        List<WebElement> valuesFromListBox = listBox.findElements(By.tagName("option"));

        for (WebElement k : valuesFromListBox){
            System.out.println("Values " + k.getText());
        }
        //listBox.click();
        listBox.sendKeys("Variant value 1");
*/

        //   listBox.click();
        //  listBox.findElement(By.xpath("//a[text()='Variant value 1']")).click();

        //var.findElement(By.xpath("//*[@class='label' and contains(text(),'Variant')]"))
        //    System.out.println(var.findElement(By.xpath("//*[@class='label' and contains(text(),'Variant type 1')")).getText());


        //  filters.setVariantTypeById(Config.getProperty("by_Id.VariantBox2"),"");

        // 8.a Check that when variant type ‘Variant type 1’ = ‘Variant value 1’ is selected, then the unavailable values from the other dropdowns are grayed out:
        filters.getVariantsOptionsById(Config.getProperty("by_Id.VariantBox2"), "grey");

        //9. Now select a value for ‘Variant type 1’ = ‘Variant value 1’ and a value for ‘Variant type 2’ = ‘Variant value 2’
        filters.setVariantTypeById(Config.getProperty("by_Id.VariantBox2"), "Variant value 2");
        filters.setVariantTypeById(Config.getProperty("by_Id.VariantBox3"), "Variant value 3");



        product.getPrices("","");
//        System.out.println(product.getPrice().toString());
  //      System.out.println(product.getOldPrice().toString());


        System.out.println(product.isAvailableByXpath(Config.getProperty("by_xPathIsAvailable")));
        System.out.println(product.getStockByXpath("//span[contains(@class,'_inventory')]").toString());
        System.out.println(product.getPointsAmountByXpath(Config.getProperty("by_xPathPointsAmount")));

        System.out.println(product.getManufacturerByCSS(Config.getProperty("by_css.Manufacturer")));
        System.out.println(product.getArtNrByXpath(Config.getProperty("by_xPath.ArtNumTitle"),Config.getProperty("by_xPath.ArnNumValue")));
        System.out.println(product.getEANByXpath(Config.getProperty("by_xPath.EANTitle"),Config.getProperty("by_xPath.EANValue")));

        product.putToCart();
        //wait until server redirect to new page
        $(By.xpath(Config.getProperty("by_xPathInCart"))).should(Condition.exist);
        System.out.println(url().toString());

        List<String> firstList = new ArrayList<String>();
        firstList.add("A");
        firstList.add("B");
        firstList.add("C");

        List<String> secondList = new ArrayList<String>();
        secondList.add("A");
        secondList.add("C");
        secondList.add("B");

        Collections.sort(firstList);
        Collections.sort(secondList);

        assertThat(firstList).as("Some list").isEqualTo(secondList);


        try {
        } catch (Exception e) {

        }
    }


    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "csv2HashDataProvider", dependsOnMethods = {"runTest"})
    @TestDataSource(csv = "/CSVData/TestFilters.csv", csvDelimiter = ";")
    public void checkFilterAvailable() {
        try {
            List<String> rs = filters.getItemFromFilter(Config.getProperty("by_xPath.Filter"), Config.getProperty("by_xPath.FilterActiveElements"));

            filters.setListBoxFilter(wb, Config.getProperty("by_xPath.Filter"), "Sofort-Lieferbar");
            //  $(By.xpath("//*[text()='Filter']")).shouldNot(Condition.exist);
            String namef = filters.checkFilterValue(Config.getProperty("by_xPath.FilterActive"));
            System.out.println("Value from filter " + namef);
        } catch (Exception e) {
            System.out.println("Cannot run test case");
        }
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "csv2HashDataProvider", dependsOnMethods = {"runTest"})
    @TestDataSource(csv = "/CSVData/TestVariantFilter.csv", csvDelimiter = ";")
    public void checkFilterBrands() {


    }


    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "csv2HashDataProvider", dependsOnMethods = {"runTest"})
    @TestDataSource(csv = "/CSVData/TestVariantFilter.csv", csvDelimiter = ";")
    public void VariantTest(HashMap<String, String> testData) {
       // WebDriver selD = getWebDriver();


    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "csv2HashDataProvider", dependsOnMethods = {"VariantTest"})
    @TestDataSource(csv = "/CSVData/TestVariantFilter.csv", csvDelimiter = ";")
    public void checkAddToCart() {


    }


    @AfterSuite
    public void shoutDown() {
        //     wb.quit();
    }
}