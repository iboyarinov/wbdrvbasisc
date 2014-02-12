package com.selenium.webdriver;

import com.codeborne.selenide.impl.WebElementsCollection;
import com.selenium.webdriver.basics.Config;
import com.selenium.webdriver.basics.Driver;
import com.selenium.webdriver.basics.db.initDB;
import com.selenium.webdriver.basics.testng.TestDataProvider;
import com.selenium.webdriver.basics.testng.TestDataSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        String[] tables = Config.getProperty("dbtables").split(";");
        int d = tables.length;


        try {
            wb = Driver.getDriver();
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

        //WebElement list = wb.findElement(By.className("brands"));
        WebElement list = wb.findElement(By.xpath("//*[text()='Marken']"));
        list.click();
        List<WebElement> values = list.findElements(By.xpath("//a[contains(@href,'brand=')]"));
        System.out.println("Size is " + values.size());
        for(WebElement value : values){
            System.out.println(value.getText());
        }

        wb.findElement(By.linkText("Timberland")).click();

        List<String> firstList = new ArrayList<String>();
        firstList.add("A");
        firstList.add("B");
        firstList.add("C");

        List<String> secondList = new ArrayList<String>();
        secondList.add("A");
        secondList.add("C");
        secondList.add("C");

        assertThat(firstList).as("Some list").containsAll(secondList);

        try {
        } catch (Exception e) {

        }
    }

    @AfterSuite
    public void shoutDown(){
        wb.close();
    }
}