package com.selenium.webdriver.basics.testng;

import au.com.bytecode.opencsv.CSVReader;
import com.selenium.webdriver.basics.Config;
import org.testng.annotations.DataProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Hedg on 11.02.14.
 */
public class TestDataProvider {

    @DataProvider(name = "csv2HashDataProvider")
    public static Iterator<Object[]> csv2HashDP(Method method) throws IOException {

        // Check method input arguments, only implementations of java.util.Map allowed
        Class[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0)
            throw new RuntimeException("Test method must contains at least 1 input parameter");
        for (Class parameterType : parameterTypes)
            if (!Map.class.isAssignableFrom(parameterType))
                throw new RuntimeException("All test method params must implement " + Map.class.toString() + " interface");

        // check @TestDataSource annotation values
        TestDataSource annotation = method.getAnnotation(TestDataSource.class);
        if (annotation == null)
            throw new RuntimeException("Test method must have @TestDataSource annotation");
        String csvFile = annotation.csv();
        if (csvFile.isEmpty())
            throw new RuntimeException("Specify in @TestDataSource annotation path to CSV file");
        String csvDelimiter = annotation.csvDelimiter();
        String csvCommentSymbol = annotation.csvCommentSymbol();

        // Open specified CSV
        InputStream inputStream = TestDataProvider.class.getClass().getResourceAsStream(csvFile);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        CSVReader reader = new CSVReader(inputStreamReader, csvDelimiter.charAt(0));

        List<Object[]> data = new ArrayList<Object[]>();
        boolean gotKeys = false;
        List<String> keys = new ArrayList<String>();
        String[] nextLine;
        int lineCounter = 0;

        // Read CSV line by line
        while ((nextLine = reader.readNext()) != null) {
            lineCounter++;
            // Skip empty or commented strings
            if (!((nextLine.length == 1 && nextLine[0].isEmpty()) || nextLine[0].startsWith(csvCommentSymbol))) {
                // Init keys
                if (!gotKeys) {
                    keys = Arrays.asList(nextLine);
                    gotKeys = true;
                    continue;
                }
                // Size of keys and data chunks in string must be the same
                else if (nextLine.length != keys.size())
                    throw new RuntimeException("Keys and data chunks quantity mismatch for row with number " + lineCounter);
                // Pack to hash and add to returned data
                HashMap<String, String> hashMap = new HashMap<String, String>();
                for (int i = 0; i < keys.size(); i++) {
                    hashMap.put(keys.get(i), nextLine[i]);
                }
                data.add(new Object[]{hashMap});
            }
        }

        return data.iterator();
    }

}
