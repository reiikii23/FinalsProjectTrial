package com.finalsproject.finalsproject;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection connectDB(){

        try{
            Class.forName("com.mysql.jdbc.Driver");

            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/", "root","");
            return connect;
        }catch(Exception e ) {
            e.printStackTrace();

        }
        return null;
    }
}
