package com.selenium.webdriver.basics;

import au.com.bytecode.opencsv.CSVReader;
import org.testng.annotations.DataProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Hedg on 11.02.14.
 */
public class TestDataProvider {
    @DataProvider
    public static Iterator<Object[]> csv2HashDP() throws IOException {
        List<Object[]> data = new ArrayList<Object[]>();
        //csv parameters
        String csvDelimiter = ";";
        String csvCommentSymbol = "--";
        CSVReader reader = null;

        // Open specified CSV
        try {
            InputStream inputStream = new FileInputStream(Config.getProperty(""));
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            reader = new CSVReader(inputStreamReader, csvDelimiter.charAt(0));
        } catch (IOException e) {
            System.out.println("Cannot find file " + e.getMessage());
        } catch (Exception fl) {
            System.out.println("Cannot open file " + fl.getMessage());
        }

        String[] nextLine;
        int lineCounter = 0;
        boolean gotKeys = false;
        List<String> keys = new ArrayList<String>();

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
                for (int i = 0; i < keys.size(); i++) hashMap.put(keys.get(i), filter(nextLine[i]));
                data.add(new Object[]{hashMap});
            }
        }

        return data.iterator();
    }

}
