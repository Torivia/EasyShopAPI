package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.beans.factory.annotation.Autowired;


@RestController
@CrossOrigin
@PreAuthorize("permitAll()")
@RequestMapping("/categories")

public class CategoriesController {

    private final CategoryDao categoryDao;
    private final ProductDao productDao;
    //I WILL BE ABLE TO USE THESE TO REFERENCE METHODS
    // Constructor-based Dependency Injection
    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    // GET method to retrieve all categories
    @GetMapping("")
    public List<Category> getAll() {
        // Retrieve and return all categories
        return categoryDao.getAllCategories();
    }
//done, NOTE if you dont put any annotation afterwards, you will get "AMBIGUOUS MAPPING" error
    @GetMapping("/{id}")
    public Category getById(@PathVariable int id)
    {
        // getting category id ( im going to reference categoryDao, which will reference MySqlCategoryDao
        //this wasnt done yet
        return categoryDao.getById(id);
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // this gets a list of product by categoryId
        //done
        return productDao.listByCategoryId(categoryId);
    }

    // add annotation to call this method for a POST action
    @PostMapping("")
    //done, this lets only admin run this
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Category addCategory(@RequestBody Category category)
    {
        // this inserts the category
        //checking if its already implemented
        return categoryDao.create(category);
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId ({id})
    //TODO: find out what annotation i need after @PutMapping
    @PutMapping("/{id}")
    // done, added annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {//done, this had a bug in it that was using .create instead of .update
        try
        {
            categoryDao.update(id, category);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // done, added annotation to call this method for a DELETE action - the url path must include the categoryId ({id})
    @DeleteMapping("/{id}")
    // done, added annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCategory(@PathVariable int id)
    {
        try
        {
            var product = categoryDao.getById(id);

            if(product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            categoryDao.delete(id);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
