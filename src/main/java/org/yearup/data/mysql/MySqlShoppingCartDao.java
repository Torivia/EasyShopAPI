package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.controllers.ShoppingCartController;
import org.yearup.data.ProductDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;
import static org.yearup.data.mysql.MySqlProductDao.mapRow;
@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    //sql commands go in here, call from shopping cart and shopping cart item\
    private final ProductDao productDao;

    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        //TODO find out what this means:
        super(dataSource);
        this.productDao = productDao;
    }


    //    @Override
//    public ShoppingCart getByUserId(int userId) {
//        String sql = "SELECT * FROM shopping_cart WHERE user_id = ?";
//        try (Connection connection = getConnection()) {//TODO find out how to MAP
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setInt(1, userId); //need
//
//            ResultSet row = statement.executeQuery();
//
//            if (row.next()) {
//                return mapRows(row);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return null;
//    }
    @Override
    public ShoppingCart getByUserId(int userId) {
        String sql = "SELECT * FROM shopping_cart WHERE user_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet row = statement.executeQuery();

            // Map the rows and return the first ShoppingCart for this userId
            List<ShoppingCartItem> shoppingCarts = mapRows(row);
//            return shoppingCarts.isEmpty() ? null : shoppingCarts.get(0);
            // Create and add the shopping cart item
            ShoppingCartItem item = new ShoppingCartItem();
//            Product product = productDao.getById(productId);
//            item.setProduct(product);
//            item.setQuantity(1);
            Map<Integer, ShoppingCartItem> items = new HashMap<>();
            ShoppingCart shoppingCart = new ShoppingCart();
            for (ShoppingCartItem shoppingCartItem : shoppingCarts ) {
                items.put(shoppingCartItem.getProductId(), shoppingCartItem);
            }
//            for (int i = 0; i < shoppingCarts.size(); i++) {
//                items.put(i, item);
//            }

            // Use setItems to update the shopping cart's items map
            shoppingCart.setItems(items);
            return shoppingCart; //THE SHOPPING CART
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public ShoppingCart addingItems(int userId, int productId) {
//        String sql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
//        ShoppingCart shoppingCart;
//        try (Connection connection = dataSource.getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
//
//            statement.setInt(1, userId);
//            statement.setInt(2, productId);
//            statement.setInt(3, 1);
//            shoppingCart = new ShoppingCart(userId, productId, 1);
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return shoppingCart;
//    }
    @Override
    public ShoppingCart addingItems(int userId, int productId) {
        String sql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
        ShoppingCart shoppingCart = new ShoppingCart();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Set parameters for the SQL query
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.setInt(3, 1); // Default quantity to 1
            //statement.executeUpdate();
            // Execute the insert operation
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error while adding items to the shopping cart.", e);
        }
    return getByUserId(userId);
    }


    @Override
    public void updateCart(int userId, int productId) {
        //sadfaajksjf
    }

//    @Override
//    public void deleteCart(int userId) {
//        //TODO check user id, delete WHERE statement DELETE FROM
//        String sql = "DELETE FROM shopping_cart\n" +
//                "WHERE user_id = ?";
//        try (Connection connection = getConnection()) {
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setInt(1, userId);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//        throw new RuntimeException(e);
//        }
//    }
    @Override
    public void deleteCart(int userId) {
        // SQL query to delete all entries for a specific user_id
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the user ID in the prepared statement
            statement.setInt(1, userId);

            // Execute the DELETE query
            int affectedRows = statement.executeUpdate();

            // Optional: Log or handle cases where no rows were deleted
            if (affectedRows == 0) {
                System.out.println("No cart found for user ID: " + userId);
            } else {
                System.out.println("Deleted " + affectedRows + " items from the cart for user ID: " + userId);
            }
        } catch (SQLException e) {
            // Throw a runtime exception with a meaningful message
            throw new RuntimeException("Failed to delete the shopping cart for user ID: " + userId, e);
        }
    }
    protected List<ShoppingCartItem> mapRows(ResultSet row) throws SQLException {
        List<ShoppingCartItem> shoppingCarts = new ArrayList<>();

        while (row.next()) {
            Product product = productDao.getById(row.getInt("product_id"));
            ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
            shoppingCartItem.setProduct(product);
            shoppingCartItem.setQuantity(row.getInt("quantity"));
            shoppingCarts.add(shoppingCartItem);
        }
        return shoppingCarts;
    }
}









