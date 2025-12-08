package com.alpha.product_management;

import com.alpha.product_management.util.DatabaseConnection;
import com.alpha.product_management.DAO.ProductCategoryDAO;
import com.alpha.product_management.entity.Product;
import com.alpha.product_management.entity.Category;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class ProductManagementApplication {
    public static void main(String[] args) {

        DatabaseConnection dbConnection = new DatabaseConnection();

        System.out.println("Test de connexion à la base de données:");
        dbConnection.testConnection();

        ProductCategoryDAO dao = new ProductCategoryDAO(dbConnection);

        System.out.println("\nToutes les catégories:");
        List<Category> categories = dao.findAllCategories();
        for (Category cat : categories) {
            System.out.println(" - " + cat.getId() + ": " + cat.getName());
        }

        System.out.println("\nProduits page 1 (taille 2):");
        List<Product> page1 = dao.findProductsPaginated(1, 2);
        for (Product prod : page1) {
            System.out.println(" - " + prod.getId() + ": " + prod.getName() + " - " + prod.getPrice() + "€");
        }

        System.out.println("\nProduits page 2 (taille 2):");
        List<Product> page2 = dao.findProductsPaginated(2, 2);
        for (Product prod : page2) {
            System.out.println(" - " + prod.getId() + ": " + prod.getName() + " - " + prod.getPrice() + "€");
        }


        System.out.println("\nRecherche: produits contenant 'Dell':");
        List<Product> dellProducts = dao.searchProductsByCriteria("Dell", null, null, null);
        for (Product prod : dellProducts) {
            System.out.println(" - " + prod.getId() + ": " + prod.getName());
        }

        System.out.println("\nRecherche: catégorie contenant 'info':");
        List<Product> infoProducts = dao.searchProductsByCriteria(null, "info", null, null);
        for (Product prod : infoProducts) {
            System.out.println(" - " + prod.getId() + ": " + prod.getName());
        }

        Instant debut = LocalDateTime.of(2024, 2, 1, 0, 0).toInstant(ZoneOffset.UTC);
        Instant fin = LocalDateTime.of(2024, 3, 1, 23, 59).toInstant(ZoneOffset.UTC);

        System.out.println("\nRecherche: produits créés entre février et mars 2024:");
        List<Product> produitsParDate = dao.searchProductsByCriteria(null, null, debut, fin);
        for (Product prod : produitsParDate) {
            System.out.println(" - " + prod.getId() + ": " + prod.getName() + " - " + prod.getCreationDateTime());
        }

        System.out.println("\nRecherche + Pagination: 'Dell', page 1, taille 5:");
        List<Product> dellPagine = dao.searchProductsByCriteria("Dell", null, null, null, 1, 5);
        for (Product prod : dellPagine) {
            System.out.println(" - " + prod.getId() + ": " + prod.getName());
        }

        System.out.println("\nRecherche + Pagination: catégorie 'informatique', page 1, taille 10:");
        List<Product> infoPagine = dao.searchProductsByCriteria(null, "informatique", null, null, 1, 10);
        for (Product prod : infoPagine) {
            System.out.println(" - " + prod.getId() + ": " + prod.getName());
        }

        System.out.println("\nFin des tests.");
    }
}