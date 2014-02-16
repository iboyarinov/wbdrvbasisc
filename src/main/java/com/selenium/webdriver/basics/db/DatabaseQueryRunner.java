package com.selenium.webdriver.basics.db;

/**
 * Created by Hedg on 11.02.14.
 */

import com.selenium.webdriver.basics.Config;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class DatabaseQueryRunner {
    private Connection connection = null;
    private final ResultSetHandler<List<Map<String, Object>>> resultSetHandler = new MapListHandler();
    private final QueryRunner queryRunner = new QueryRunner();
    private static DatabaseQueryRunner instance = null;

    public static synchronized DatabaseQueryRunner getInstance() {
        if (instance == null) {
            instance = new DatabaseQueryRunner();
        }
        return instance;
    }

    private DatabaseQueryRunner() {
    }

    private void openDBConnection() throws ClassNotFoundException, SQLException {
        try {
            Class.forName(Config.getProperty("db.driver"));
            connection = DriverManager.getConnection(Config.getProperty("db.path"));
        } catch (Exception e) {
            System.out.println("Cannot load db properties " + e.getMessage());
        }
    }

    public void closeDBConnection() {

        try {
            if (connection != null)
                if (!connection.isClosed()) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> runQuery(String sqlQuery) throws SQLException {


        List<Map<String, Object>> queryResult = null;
        try {
            openDBConnection();
            queryResult = queryRunner.query(connection, sqlQuery, resultSetHandler);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeDBConnection();
        }
        return queryResult;
    }

    public List<Object> runQuery(String sqlQuery, String columnName) throws SQLException {

        List<Object> queryResult = null;
        try {
            openDBConnection();
            queryResult = queryRunner.query(connection, sqlQuery, new ColumnListHandler<>(columnName));
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot run query " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeDBConnection();
        }
        return queryResult;
    }

    public List<String> runQueryStr(String sqlQuery, String columnName) throws SQLException {

        List<String> queryResult = null;
        try {
            openDBConnection();
            queryResult = queryRunner.query(connection, sqlQuery, new ColumnListHandler<String>(columnName));
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot run query " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeDBConnection();
        }
        return queryResult;
    }

}
