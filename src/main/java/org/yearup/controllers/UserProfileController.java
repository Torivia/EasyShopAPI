package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
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
    private final ProfileDao profileDao;

    @Autowired
    public UserProfileController(UserDao userDao, ProductDao productDao, ProfileDao profileDao) {
        this.userDao = userDao;
        this.productDao = productDao;
        this.profileDao = profileDao;
    }

    @GetMapping() // Added explicit path
    public Profile getProfile(Principal principal) {
        try {
            // Get the currently logged in username
            String username = principal.getName(); // Find database user by username (not by hardcoded ID)
            User user = userDao.getByUserName(username);
            int userId = user.getId();
            return profileDao.getProfileByUserId(userId);

        } catch (Exception e) {
            // Add proper error handling
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving Profile");
        }
    }

    @PutMapping()
    public Profile updateProfile(@RequestBody Profile updatedProfile, Principal principal) {
        try {
            // Get the currently logged-in username
            String username = principal.getName();
            User currentUser = userDao.getByUserName(username);

            if (currentUser.getId() != updatedProfile.getUserId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own profile.");
            }

            // Call the DAO's update method
            return profileDao.update(updatedProfile);
        } catch (ResponseStatusException e) {
            // Rethrow exceptions like forbidden access
            throw e;
        } catch (Exception e) {
            // Handle any other exceptions
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating profile", e);
        }
//    }
//            String username = principal.getName();
//
//            // Ensure the updated user object has the correct username
//            updatedProfile.setUsername(username);
//
//            // Call the DAO's update method to update the user's information
//            return ProfileDao.update(updatedUser);
//
//        } catch (Exception e) {
//            // Handle exceptions properly
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating profile", e);
//        }
//    }

    }

}
