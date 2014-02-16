package com.selenium.webdriver;

import com.codeborne.selenide.Condition;
import com.selenium.webdriver.basics.Config;
import com.selenium.webdriver.basics.db.DatabaseQueryRunner;
import com.selenium.webdriver.basics.testng.TestDataProvider;
import com.selenium.webdriver.basics.testng.TestDataSource;
import com.selenium.webdriver.page.filters;
import com.selenium.webdriver.page.product;
import org.openqa.selenium.By;
import org.testng.annotations.*;

import java.sql.SQLException;
import java.util.*;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.filter;

/**
 * Created by Hedg on 11.02.14.
 */
public class pageTest {

    @BeforeSuite
    public void setUp() throws Exception {

        try {
            //if needed to set up implicit delay
            // WebDriver wb = getWebDriver();
            // wb.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("Couldn't start WebDriver " + e.getMessage());
        }
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "csv2HashDataProvider")
    @TestDataSource(csv = "/CSVData/SimpleTest.csv", csvDelimiter = ";")

    public void runTest(HashMap<String, String> testData) throws Exception {
        final DatabaseQueryRunner dbRunner = DatabaseQueryRunner.getInstance();
        //getting data for test from data provider
        String[] startURL = testData.get("startURL").split(";");
        String[] landingURL = testData.get("landingURL").split(";");
        String[] categoryName = testData.get("categoryName").split(";");

        //1.    Go to http://test.rakuten-shop.de
        open(startURL[0]);
        //2.    Select category ‘Teste’ - http://test.rakuten-shop.de/teste-41755160/
        $(By.linkText(categoryName[0])).click();

        assertThat(url()).as("Actual URL").isEqualTo(landingURL[0]);

    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "csv2HashDataProvider", dependsOnMethods = {"runTest"})
    @TestDataSource(csv = "/CSVData/TestFilters.csv", csvDelimiter = ";")
    public void checkFilterAvailable(HashMap<String, String> testData) {
        String[] filterValues = testData.get("filterValues").split(";");

        try {
            //2 b.   Check the Filter values: Versandkostenfrei = 15 & Reduziert (products.price < products.old_price) = 14 & Sofort-Lieferbar (products.available = ‚1‘) = 31
            List<String> valuesFromPage = filters.getItemFromFilter(Config.getProperty("by_xPath.Filter"), Config.getProperty("by_xPath.FilterActiveElements"));
            List<String> valuesFromTestFile = Arrays.asList(filterValues[0].split(":"));

            Collections.sort(valuesFromPage);
            Collections.sort(valuesFromTestFile);

            assertThat(valuesFromTestFile).as("Compare filter values from test file and page").isEqualTo(valuesFromPage);

            //3.    Filter by Sofort-Lieferbar
            //a.        How would you check if the filter is applied (no tests to write here – just ideas)
            // I'm apologize but I decided to implement this
            //if one of value was selected it getting class = active
            filters.setListBoxFilter(Config.getProperty("by_xPath.Filter"), "Sofort-Lieferbar");
            //  $(By.xpath("/[text()='Filter']")).shouldNot(Condition.exist);
            String namef = filters.checkFilterValue(Config.getProperty("by_xPath.FilterActive"));

            filters.resetFilterByXpath(Config.getProperty("by_xPath.FilterActive"));


        } catch (SQLException e) {

        } catch (Exception e) {

        }
    }


    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "csv2HashDataProvider", dependsOnMethods = {"checkFilterAvailable"})
    @TestDataSource(csv = "/CSVData/testFilterBrands.csv", csvDelimiter = ";")
    public void checkFilterBrand(HashMap<String, String> testData) {
        final DatabaseQueryRunner dbRunner = DatabaseQueryRunner.getInstance();
        String[] filterValue = testData.get("filterValue").split(";");

        try {
            //get brand from filter Marken
            List<String> brandsFromFilter = filters.getItemFromFilter(Config.getProperty("by_xPath.FilterBrands"), Config.getProperty("by_xPath.FilterBrandsElements"));
            Collections.sort(brandsFromFilter);

            //getting brands from DB
            List<String> brandsFromDB = dbRunner.runQueryStr("SELECT BRAND_NAME FROM BRANDS", "BRAND_NAME");
            Collections.sort(brandsFromDB);

            //compare Brands against db
            assertThat(brandsFromDB).as("Compare brands against DB").isEqualTo(brandsFromFilter);


            //4.    Filter by Marken = ‘Timberland’
            $(By.linkText(filterValue[0])).click();

            //4 a.        Check that only 1 product is displayed (Check against DB data.)
            List<String> quantityOfProduct = product.getProductsFromPage(Config.getProperty("by_xPathProducts"));
            assertThat(quantityOfProduct.size()).as("Compare quantity of product on page").isEqualTo(1);

            //4 b. Check the reduced percent for the product (Check against DB data.)
            Double price = product.getPrice(Config.getProperty("by_xPathPriceAB"));
            Double oldPrice = product.getOldPrice("");

            //calculation of price reduce
            //Double reducePrice = ((price / oldPrice) - 1) * 100;

            List<Object> percentReduceFromDB = dbRunner.runQuery("select ROUND(((pv.price/pv.old_price)-1)*100) as present_reduce from PRODUCT_VARIANTS pv\n" +
                    "JOIN PRODUCTS p ON p.ID = pv.PRODUCT_ID\n" +
                    "JOIN BRANDS b ON b.ID = p.BRAND_ID\n" +
                    "WHERE b.BRAND_NAME = 'Timberland' and pv.price =" + price + " and pv.old_price = " + oldPrice, "present_reduce");

            assertThat(product.reducePercent(Config.getProperty("by_xPathPriceReduce"))).as("Compare webpage and DB").isEqualTo(percentReduceFromDB.get(0));


            //5. Select the only product: http://www.rakuten.de/produkt/timberland-testing-umlauts-oeaez23gf-1071535370.html
            product.clickOnProductByName("Testing umlauts - öäz23gf");

        } catch (SQLException e) {

            System.out.println("DB exception +" + e.getMessage());

        } catch (Exception e) {

            System.out.println("Cannot run test ");

        }

    }

    @Test(dependsOnMethods = {"checkFilterBrand"})
    public void checkProductVariants() {
        final DatabaseQueryRunner dbRunner = DatabaseQueryRunner.getInstance();
        try {

            //6.    Notice that the product has 3 variant types: Variant type 1, Variant type 2 and Variant type 3 (Check against DB)
            List<String> variantTypesFromPage = filters.getAllProductTypesByXpath(Config.getProperty("by_xPathProductVariants"));

            //get price for SQL query
            Double price = product.getPrice(Config.getProperty("by_xPathPriceAB"));
            //get brand for SQL query

            String BrandName = product.getManufacturerByCSS(Config.getProperty("by_css.Manufacturer"));


            List<Map<String, Object>> valuesFromDB = dbRunner.runQuery("SELECT pv.variation1_type, pv.variation2_type, pv.variation3_type FROM PRODUCT_VARIANTS pv\n" +
                    "JOIN PRODUCTS p ON p.ID = pv.PRODUCT_ID\n" +
                    "JOIN BRANDS b ON b.ID = p.BRAND_ID\n" +
                    "WHERE b.BRAND_NAME = '" + BrandName + "'" + " and pv.price =" + price);
            //transform data from db to List<String> and Yes, I know that it's ugly
            String[] result = valuesFromDB.get(0).values().toArray(new String[0]);
            List<String> variantTypesFromDB = Arrays.asList(result);

            Collections.sort(variantTypesFromPage);
            Collections.sort(variantTypesFromDB);

            assertThat(variantTypesFromPage).as("Compare how many variant of types have product").isEqualTo(variantTypesFromDB);


            //get values from listbox
            //7.    For variant type ‘Variant type 1’ check that there are 3 variant values: Variant value 1, Variant value 11 and Variant value 111 (Check against DB)
            List<String> variantsFromPage = filters.getVariantsById(Config.getProperty("by_Id.VariantBox1"));

            List<String> variantsType1FromDB = dbRunner.runQueryStr("SELECT pv.variation1_value FROM PRODUCT_VARIANTS pv\n" +
                    "JOIN PRODUCTS p ON p.ID = pv.PRODUCT_ID\n" +
                    "JOIN BRANDS b ON b.ID = p.BRAND_ID\n" +
                    "WHERE b.BRAND_NAME = '" + BrandName + "' ", "variation1_value");

            Collections.sort(variantsFromPage);
            Collections.sort(variantsType1FromDB);

            assertThat(variantsType1FromDB).as("Compare variants from listbox 1 against DB").isEqualTo(variantsFromPage);

            //set variant to listbox
            filters.setVariantTypeById(Config.getProperty("by_Id.VariantBox1"), "Variant value 1");

            // 8.a Check that when variant type ‘Variant type 1’ = ‘Variant value 1’ is selected, then the unavailable values from the other dropdowns are grayed out:

            List<String> variantsAvailable = filters.getVariantsOptionsById(Config.getProperty("by_Id.VariantBox2"), "black");

            assertThat("Variant value 2").as("Should be only one available value for listbox2 if lisbox1=Variant value 1").isEqualTo(variantsAvailable.get(0));

            //8.a i.       Variant type 2: ‘Variant value 2’ is black & ‘Variant value 22’ is grayed out
            List<String> variantsUnavailable = filters.getVariantsOptionsById(Config.getProperty("by_Id.VariantBox2"), "grey");

            assertThat(variantsUnavailable).as("variants from page should contain Variant value 22").contains("Variant value 22");

            //8.a ii.      Variant type 3: ‘Variant value 3’ = black (class=”rakuten_product_control_variant_value_black”) & ‘Variant value 33’ = grayed out (class=”rakuten_product_control_variant_value_grey”)
            List<String> variantsAvailable3 = filters.getVariantsOptionsById(Config.getProperty("by_Id.VariantBox3"), "black");
            assertThat("Variant value 3").as("Should be only one available value for listbox2 if lisbox1=Variant value 1").isEqualTo(variantsAvailable3.get(0));

            List<String> variantsUnavailable3 = filters.getVariantsOptionsById(Config.getProperty("by_Id.VariantBox3"), "grey");
            assertThat(variantsUnavailable3).as("variants from page should contain Variant value 33").contains("Variant value 33");

            //9. Now select a value for ‘Variant type 1’ = ‘Variant value 1’ and a value for ‘Variant type 2’ = ‘Variant value 2’
            filters.setVariantTypeById(Config.getProperty("by_Id.VariantBox2"), "Variant value 2");
            filters.setVariantTypeById(Config.getProperty("by_Id.VariantBox3"), "Variant value 3");

            //9.  i.            Price
            //9.   ii.            Reduced price
            Map<String, Object> pricesFromPage = product.getPrices(Config.getProperty("by_xPathPrice"), Config.getProperty("by_xPathOldPrice"));

            List<Map<String, Object>> pricesFromDB = dbRunner.runQuery("select pv.price as Price, pv.old_price as OldPrice FROM PRODUCT_VARIANTS pv\n" +
                    "JOIN PRODUCTS p ON p.ID = pv.PRODUCT_ID\n" +
                    "JOIN BRANDS b ON b.ID = p.BRAND_ID and b.BRAND_NAME = 'Timberland'\n" +
                    "WHERE pv.variation1_value = 'Variant value 1' and pv.variation2_value = 'Variant value 2' and pv.variation3_value = 'Variant value 3'");

            assertThat(pricesFromDB.get(0)).as("Check prices against FB").isEqualTo(pricesFromPage);

            //9. iii.            Stock (‘111 Stück auf Lager’)
            Integer productStockFromPage = product.getStockByXpath(Config.getProperty("by_xPathStock"));


            List<String> isAvailableFromDB = dbRunner.runQueryStr("select pv.stock FROM PRODUCT_VARIANTS pv\n" +
                    "JOIN PRODUCTS p ON p.ID = pv.PRODUCT_ID\n" +
                    "JOIN BRANDS b ON b.ID = p.BRAND_ID and b.BRAND_NAME = 'Timberland'\n" +
                    "WHERE pv.variation1_value = 'Variant value 1' and pv.variation2_value = 'Variant value 2' and pv.variation3_value = 'Variant value 3'", "stock");
            Integer quantity = Integer.parseInt(isAvailableFromDB.get(0));

            assertThat(quantity).as("Check if product is available against DB").isEqualTo(productStockFromPage);

            //9. iv.            Superpoints no (there is a Superpunkte box in div id="product_desc" ) – 1 Euro = 1 SP
            List<String> actualPriceFromDB = dbRunner.runQueryStr("select pv.price FROM PRODUCT_VARIANTS pv\n" +
                    "JOIN PRODUCTS p ON p.ID = pv.PRODUCT_ID\n" +
                    "JOIN BRANDS b ON b.ID = p.BRAND_ID and b.BRAND_NAME = 'Timberland'\n" +
                    "WHERE pv.variation1_value = 'Variant value 1' and pv.variation2_value = 'Variant value 2' and pv.variation3_value = 'Variant value 3'", "Price");

            Integer points = product.getPointsAmountByXpath(Config.getProperty("by_xPathPointsAmount"));
            Integer pointsFromDB = Integer.parseInt(actualPriceFromDB.get(0));

            assertThat(pointsFromDB).as("1 Euro = 1 SP").isEqualTo(points);

            //9.   v.            Produktdaten box: Hersteller & EAN & Art. Nr
            String manufacturerFromPage = product.getManufacturerByCSS(Config.getProperty("by_css.Manufacturer"));
            Map<String,String> articleFromPage = product.getArtNrByXpath(Config.getProperty("by_xPath.ArtNumTitle"), Config.getProperty("by_xPath.ArnNumValue"));
            Map<String,String> EANFromPage = product.getEANByXpath(Config.getProperty("by_xPath.EANTitle"), Config.getProperty("by_xPath.EANValue"));

            Map<String,Object> dataFromPage = new HashMap<>();
            dataFromPage.put("BrandName", manufacturerFromPage);
            dataFromPage.putAll(articleFromPage);
            dataFromPage.putAll(EANFromPage);

            List<Map<String, Object>> productDataBox = dbRunner.runQuery("select b.brand_name as BrandName ,pv.ArtNum as ArnNum, pv.EAN as EAN FROM PRODUCT_VARIANTS pv\n" +
                    "JOIN PRODUCTS p ON p.ID = pv.PRODUCT_ID\n" +
                    "JOIN BRANDS b ON b.ID = p.BRAND_ID and b.BRAND_NAME = 'Timberland'\n" +
                    "WHERE pv.variation1_value = 'Variant value 1' and pv.variation2_value = 'Variant value 2' and pv.variation3_value = 'Variant value 3'");


        assertThat(productDataBox.get(0)).as("Compare BrandName,ArtNum and EAN againt DB").isEqualTo(dataFromPage);

        } catch (SQLException e) {

            System.out.println("DB exception in filter variant test +" + e.getMessage());

        } catch (Exception e) {

            System.out.println("Cannot run filter variant Test " + e.getMessage());

        }

    }


    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "csv2HashDataProvider", dependsOnMethods = {"checkProductVariants"})
    @TestDataSource(csv = "/CSVData/TestAddtoCart.csv", csvDelimiter = ";")
    public void addToCartTest(HashMap<String, String> testData) {
        String[] landingURL = testData.get("landingURL").split(";");

        try {
            //10. Click on ‚In den Warenkorb’ button.
            //10 a.        Check that user is redirected to http://www.rakuten.de/shopcart - please do not continue with the order as the environment is production
            product.putToCart();
            //wait until server redirect to new page
            $(By.xpath(Config.getProperty("by_xPathInCart"))).should(Condition.exist);
            System.out.println(url().toString());
            assertThat(url()).as("Compare url from page and test file").isEqualTo(landingURL[0]);

        } catch (Exception e) {

            System.out.println("Cannot run add to cart test " + e.getMessage());

        }
    }


    @AfterSuite
    public void shoutDown() {
        close();
    }
}