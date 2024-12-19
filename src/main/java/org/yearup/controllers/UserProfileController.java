package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@CrossOrigin
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")

public class UserProfileController {
    private final UserDao userDao;
    private final ProductDao productDao;

    @Autowired
    public UserProfileController(UserDao userDao, ProductDao productDao) {
        this.userDao = userDao;
        this.productDao = productDao;
    }
    @GetMapping() // Added explicit path
    public User getProfile(Principal principal) {
        try {
            // Get the currently logged in username
            String username = principal.getName(); // Find database user by username (not by hardcoded ID)
            User user = userDao.getByUserName(username);
            int userId = user.getId();
            return userDao.getUserById(userId);

        } catch (Exception e) {
            // Add proper error handling
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving Profile");
        }
    }
}
