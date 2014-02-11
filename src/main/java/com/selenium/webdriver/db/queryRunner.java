package com.selenium.webdriver.db;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by Hedg on 11.02.14.
 */
public class queryRunner {

    public ResultSet runQuery(Statement st, String query) throws SQLException {
        ResultSet rs = st.executeQuery(query);

        return rs;
    }

}
