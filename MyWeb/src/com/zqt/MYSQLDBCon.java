package com.zqt;

import java.sql.Connection;
import java.sql.DriverManager;

public class MYSQLDBCon {
    private static Connection conn = null;

    public static Connection getCon() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String user = "root";
            String pwd = "admin";
            String url = "jdbc:mysql://localhost:3306/new_schema";
            conn = DriverManager.getConnection(url, user, pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

}
