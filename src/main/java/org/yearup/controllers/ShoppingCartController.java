package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;
//NOTE ask remsey if Shopping cart controller needs a MySqlDao thingy like the rest; yes it does

// done, converted this class to a REST controller
@RestController
//done, only logged in users should have access to these actions
@PreAuthorize("hasRole('ROLE_USER')")
//NOTE putting this in as a url start
@RequestMapping("/cart")
public class ShoppingCartController
{

    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

//    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
//        this.shoppingCartDao = shoppingCartDao;
//        this.userDao = userDao;
//        this.productDao = productDao;
//    }

    // each method in this controller requires a Principal object as a parameter
    //FIXME not sure what mapping is needed, if any
    @GetMapping()
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the shoppingcartDao to get all items in the cart and return the cart
            return null;//ShoppingCartDao.getByUserId(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad. Something went wrong with getting the cart!");
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
@PostMapping("/products/{id}")
    //NOTE does not need Preauthorize

    public ShoppingCart addToCart(Principal principal, @PathVariable int id)
    {//add necessary stuffs
        return null;
    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    @PutMapping("/products/{id}")
    public void updateCart(Principal principal,@PathVariable int id)
    {//add necessary stuffs
        // needs @RequestBody

    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart
    @DeleteMapping("")
    public void deleteCart(Principal principal)
    {
        //a
    }

}
