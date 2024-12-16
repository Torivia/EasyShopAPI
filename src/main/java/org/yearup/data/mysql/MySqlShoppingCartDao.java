package org.yearup.data.mysql;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class MySqlShoppingCartDao {
    //sql commands go in here, call from shopping cart and shopping cart item\
    /*
    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }
    @Override
    public ShoppingCart getByUserId(int userId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productId);

            ResultSet row = statement.executeQuery();

            if (row.next())
            {
                return mapRow(row);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }
         */
}
