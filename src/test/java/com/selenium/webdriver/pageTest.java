package com.selenium.webdriver;

import com.selenium.webdriver.basics.db.initDB;
import com.selenium.webdriver.basics.testng.TestDataProvider;
import com.selenium.webdriver.basics.testng.TestDataSource;
import com.selenium.webdriver.page.product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.assertj.core.api.Assertions.assertThat;

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
            wb = getWebDriver();
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
        String[] startURL = testData.get("").split(",");

        wb.get("http://test.rakuten-shop.de/teste-41755160/");
        WebElement category = wb.findElement(By.linkText("Teste"));
        category.click();
        wb.getCurrentUrl();


        WebElement filter = wb.findElement(By.xpath("//*[text() = 'Filter']"));

        List<WebElement> filterValues = filter.findElements(By.xpath("//*[contains(text(),'Versandkostenfrei') or contains(text(),'Reduziert ') or contains(text(),'Sofort-Lieferbar')]"));
        System.out.println("Size of " + filterValues.size());


        //WebElement list = wb.findElement(By.className("brands"));
        WebElement list = wb.findElement(By.xpath("//*[text()='Marken']"));
        list.click();
        List<WebElement> values = list.findElements(By.xpath("//a[contains(@href,'brand=')]"));
        System.out.println("Size is " + values.size());
        for (WebElement value : values) {
            System.out.println(value.getText());
        }


        wb.findElement(By.linkText("Timberland")).click();


/*
        WebElement checkboxList = wb.findElement(By.linkText("Filter"));
        checkboxList.click();

       List<WebElement> chekboxes = checkboxList.findElements(By.xpath("//div[@id='plst_toolbar']/div[4]/div/ul/li"));

        for (WebElement value : chekboxes){
           System.out.println(value.getText());
      }
*/
        product.clickOnProductByName(wb, "Testing umlauts - öäz23gf");


        //find how many variants available
        WebElement var = wb.findElement(By.xpath("//div[@class='variants']"));
        //  System.out.println(var.getText());

        //find first listbox
        //       WebElement las = var.findElement(By.xpath("//*[@class='label' and contains(text(),'Variant type')]"));
        //       System.out.println("Single element " + las.getText());
        List<WebElement> variants = var.findElements(By.xpath("//*[@class='label' and contains(text(),'Variant type')]"));

        //catch all elements
        for (WebElement varType : variants) {

            // List<WebElement> types = varType.findElements(By.xpath("//*[@id='rakuten_control_variant_1']"));


            System.out.println(varType.getText());

        }
        //get values from listbox 1

        Select list1 = new Select(wb.findElement(By.id("rakuten_control_variant_1")));
        list1.selectByVisibleText("Variant value 1");

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

        WebElement listBox2 = wb.findElement(By.xpath("//*[@id='rakuten_control_variant_2']"));
        listBox2.click();


        // List<WebElement> valuesFromListBox2 = listBox2.findElements(By.tagName("option"));
        final Select combobox2 = new Select(wb.findElement(By.xpath("//*[@id='rakuten_control_variant_2']")));
        List<WebElement> vals = combobox2.getOptions();
        for (WebElement df : vals) {
            System.out.println("Class for combobx2 " + df.getAttribute("class"));
        }
        // System.out.println("Combobox "+combobox2.getOptions());


        List<WebElement> valuesFromListBox2 = listBox2.findElements(By.xpath("option"));
        for (WebElement k : valuesFromListBox2) {
            System.out.println("Options " + k.getText() + " " + k.getAttribute("class"));
        }

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
    @TestDataSource(csv = "/CSVData/TestVariantFilter.csv", csvDelimiter = ";")
    public void VariantTest(HashMap<String, String> testData) {
      WebDriver selD =  getWebDriver();


    }




    @AfterSuite
    public void shoutDown() {
        //     wb.quit();
    }
}