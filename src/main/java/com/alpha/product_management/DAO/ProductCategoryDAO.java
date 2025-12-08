package com.alpha.product_management.DAO;

import com.alpha.product_management.entity.Product;
import com.alpha.product_management.entity.Category;
import com.alpha.product_management.util.DatabaseConnection;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDAO {
    private final DatabaseConnection dbConnection;


    public ProductCategoryDAO(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }


    public List<Category> findAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT id, name FROM product_category ORDER BY id";

        try (Connection connection = dbConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Category category = new Category();
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                categories.add(category);
            }
        } catch (SQLException e) {
            handleSQLException("Erreur lors de la récupération des catégories", e);
        }
        return categories;
    }


    public List<Product> findProductsPaginated(int page, int size) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT id, name, price, creation_datetime FROM product ORDER BY id LIMIT ? OFFSET ?";

        int offset = (page - 1) * size;

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, offset);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = mapResultSetToProduct(resultSet);
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            handleSQLException("Erreur lors de la pagination des produits", e);
        }
        return products;
    }

    public List<Product> searchProductsByCriteria(String productName, String categoryName,
                                                  Instant creationMin, Instant creationMax) {
        List<Product> products = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT DISTINCT p.id, p.name, p.price, p.creation_datetime " +
                        "FROM product p " +
                        "LEFT JOIN product_category pc ON p.id = pc.product_id " +
                        "WHERE 1=1"
        );

        List<Object> parameters = new ArrayList<>();

        if (productName != null && !productName.trim().isEmpty()) {
            queryBuilder.append(" AND p.name ILIKE ?");
            parameters.add("%" + productName + "%");
        }

        if (categoryName != null && !categoryName.trim().isEmpty()) {
            queryBuilder.append(" AND pc.name ILIKE ?");
            parameters.add("%" + categoryName + "%");
        }

        if (creationMin != null) {
            queryBuilder.append(" AND p.creation_datetime >= ?");
            parameters.add(Timestamp.from(creationMin));
        }

        if (creationMax != null) {
            queryBuilder.append(" AND p.creation_datetime <= ?");
            parameters.add(Timestamp.from(creationMax));
        }

        queryBuilder.append(" ORDER BY p.id");

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = mapResultSetToProduct(resultSet);
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            handleSQLException("Erreur lors de la recherche par critères", e);
        }
        return products;
    }

    public List<Product> searchProductsByCriteria(String productName, String categoryName,
                                                  Instant creationMin, Instant creationMax,
                                                  int page, int size) {

        List<Product> allFilteredProducts = searchProductsByCriteria(
                productName, categoryName, creationMin, creationMax
        );

        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, allFilteredProducts.size());

        if (startIndex >= allFilteredProducts.size()) {
            return new ArrayList<>();
        }

        return allFilteredProducts.subList(startIndex, endIndex);
    }

    private Product mapResultSetToProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getInt("id"));
        product.setName(resultSet.getString("name"));
        product.setPrice(resultSet.getDouble("price"));

        Timestamp timestamp = resultSet.getTimestamp("creation_datetime");
        if (timestamp != null) {
            product.setCreationDateTime(timestamp.toInstant());
        }

        return product;
    }

    private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }
}