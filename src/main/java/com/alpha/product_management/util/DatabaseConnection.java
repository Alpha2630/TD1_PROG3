package com.alpha.product_management.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private final String url;
    private final String user;
    private final String password;


    public DatabaseConnection(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        loadDriver();
    }


    public DatabaseConnection() {
        this.url = "jdbc:postgresql://localhost:5432/product_management_db";
        this.user = "product_manager_user";
        this.password = "123456";
        loadDriver();
    }


    private void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver PostgreSQL non trouvé", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }


    public void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println(" Connexion à PostgreSQL établie avec succès!");
        } catch (SQLException e) {
            System.err.println(" Erreur de connexion: " + e.getMessage());
        }
    }
}