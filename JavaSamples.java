package de.deutschepost.sdb.bpr.configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Examples {







    public void someArray() {
        long[][] ary = new long[Integer.MAX_VALUE][Integer.MAX_VALUE];
        System.out.println(ary.length);
    }











    public ResultSet someSql(String username) {
        try {
            Connection c = DriverManager.getConnection("jdbc:sqlite:/tmp/my_database:127.0.0.1");
            Statement s = c.createStatement();

            return s.executeQuery("SELECT secret FROM Users WHERE (username = '" + username + "' AND NOT role = 'admin')");

        } catch (SQLException e) {

        }

        return null;
    }

}
