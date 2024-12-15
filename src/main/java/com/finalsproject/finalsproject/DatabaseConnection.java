package com.finalsproject.finalsproject;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection connectDB(){

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/fx_finalproj", "reiki","1234");
            return connect;
        }catch(Exception e ) {
            e.printStackTrace();
        }
        return null;
    }
}
