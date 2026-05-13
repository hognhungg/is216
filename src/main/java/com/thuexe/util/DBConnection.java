/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thuexe.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection conn = null;

    private DBConnection() {
    }

    public static synchronized Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                String url = "jdbc:oracle:thin:@localhost:1521:orcl19"; 
                String user = "CSDL_THUEXE"; 
                String pass = "123456";      
                
                conn = DriverManager.getConnection(url, user, pass);
                System.out.println("--- Khởi tạo kết nối Singleton thành công ---");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}