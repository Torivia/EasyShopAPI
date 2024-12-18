package org.yearup.data;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    //this class is about what you can execute with shopping cart/item attributes,
    //TODO find a way to make the amount of a certain item greater (if customer adds more than one to cart
    //NOTE check the controller to see if youre correct about the data type it returns, if ur ever unsure!
    ShoppingCart addingItems(int userId, int productId);
    void updateCart(int userId, int productId);
    void deleteCart(int userId);
}
