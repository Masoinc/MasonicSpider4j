package me.masonic.datamining.Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Masonic Project
 * 2017-7-8-0008
 */
public class SqlUtility {
    private static Connection connection = null;

    public static Connection getConnection() {
        return connection;
    }

    public static void registerSQL(String DBName) {
        String URL = "jdbc:mysql://127.0.0.1:3306/" + DBName + "?useUnicode=true&characterEncoding=utf-8";
        String UNAME = "mc";
        String UPASSWORD = "492357816";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("JDBC驱动未加载");
            return;
        }
        try {
            connection = DriverManager.getConnection(URL, UNAME, UPASSWORD);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
