package com.rts.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RtsTest {
    public static void main(String[] args) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.204.121:3307/xiyue?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true", "root", "root");
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("insert into test(name) values(?)");
            pstmt.setString(1, "李四");
            pstmt2 = con.prepareStatement("insert into test(name) values (?)");
            pstmt2.setString(1,"哈士奇");
            int i = pstmt.executeUpdate();
            System.out.println(i);
            int a = 100 / 0;
            int j = pstmt2.executeUpdate();
            System.out.println(j);
            con.commit();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
