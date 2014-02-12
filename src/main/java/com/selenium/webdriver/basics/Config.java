package com.selenium.webdriver.basics;

/**
 * Created by Hedg on 11.02.14.
 */

import java.io.File;
import java.io.FilenameFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static Config instance = null;
    private Properties prop = null;
    FilenameFilter filter = null;
    FilenameFilter dirFilter = null;

    private void loadProperties(String path) throws Exception {

        try {
            File dir = new File(path);
            filter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".properties");
                }
            };
            dirFilter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return dir.isDirectory();
                }
            };
            String files[] = dir.list(filter);

            if (prop == null) {
                prop = new Properties();
            }
            if (files == null) {
                return;
            }
            for (String file : files) {
                System.out.println(file);
                File localFile = new File("properties" + System.getProperty("file.separator") + file);
                if (localFile.isDirectory()) continue;
                FileInputStream fis = new FileInputStream(localFile.getAbsolutePath());
                prop.load(fis);
                fis.close();
            }

            String dirs[] = dir.list(dirFilter);
            for (String directory : dirs) {
                loadProperties(path + "\\" + directory);
            }
        } catch (IOException e) {

        }
    }

    private Config() throws Exception {
        loadProperties("properties");
    }

    public static String getProperty(String propertyName) throws Exception {

        try {
            if (instance == null) {
                instance = new Config();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return instance.prop.getProperty(propertyName);
    }

}
