package com.zqt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBOperation {
    public static boolean query(String name, String passwd) throws SQLException {
        Connection conn = null;
        conn = MYSQLDBCon.getCon();     //db connect
        String sql = "select * from userInfo where userName=? and userPasswd=?";
        PreparedStatement pstmt = conn.prepareStatement(sql); // 创建用户操作执行SQL语句的PreparedStatement对象
        pstmt.setString(1, name);
        pstmt.setString(2, passwd);
        ResultSet rs = pstmt.executeQuery(); // 编译执行insert语句
        int id = 0;
        while (rs.next()) {
            id++;
        }
        rs.close();
        pstmt.close();
        conn.close();
        if (id > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean add(String username, String passwd) throws SQLException {
        Connection conn = null;
        conn = MYSQLDBCon.getCon();     //db connect
        String sql = "insert into userInfo(userName,userPasswd) values(?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql); // 创建用户操作执行SQL语句的PreparedStatement对象
        pstmt.setString(1, username);
        pstmt.setString(2, passwd);
        boolean rs = pstmt.execute(); // 编译执行insert语句

        pstmt.close();
        conn.close();
        return rs;
    }
}
