package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ContactProgram {
    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "hasan";
        try (Connection connection = DriverManager.getConnection(
                url, username, password
        )) {

            System.out.println("Connection to PostgreSQL server");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
