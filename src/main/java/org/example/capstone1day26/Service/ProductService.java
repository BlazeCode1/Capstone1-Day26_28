package org.example.capstone1day26.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone1day26.Model.Category;
import org.example.capstone1day26.Model.Product;
import org.example.capstone1day26.Model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class ProductService {

    ArrayList<Product> products = new ArrayList<>();
    private final CategoryService categoryService;
    private final UserService userService;
    public ArrayList<Product> getAllProducts(){
        return products;
    }

    public int addProduct(Product product){
        if(categoryService.getAllCategories().isEmpty()){
            return 1;
        }
        for (Product p : products){
            if(product.getID().equals(p.getID())){
                return 0;
            }
        }
        for (Category c : categoryService.getAllCategories()){
            if(product.getCategoryID().equals(c.getID())){
                products.add(product);
                return 3;
            }
        }
       return 2;
    }
    //to update a product, we have to check for Product ID, Category ID,
    public int updateProduct(Product product){
        if(categoryService.getAllCategories().isEmpty()){
            return 0; // category list empty
        }
        Category category = null;
        for (Category c : categoryService.getAllCategories()){ // to check if category ID is valid
            if(c.getID().equals(product.getCategoryID())){
                category = c;
            }
        }
        if(category == null){
            return 1; // if category not found
        }
        for (Product p : products){ // to check if product ID is valid
            if(product.getID().equals(p.getID())){
            products.set(products.indexOf(p),product);
            return 3;
            }
        }
        return 2;

    }

    public boolean deleteProduct(String productID){
        for (Product p : products){
            if(p.getID().equals(productID)){
                products.remove(p);
                return true;
            }
        }
        return false;
    }

    //Second Method
    public ArrayList<Product> topProductsPurchased(String adminID,String start, String end,int userLimit){
        if(!isUserAdmin(adminID)) return null; // if adminID is correct result will be false and continue with the service
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        ArrayList<Product> productsWithinRange = new ArrayList<>();

        for (Product p : products){
            if(p.getLastSoldDate() != null &&
                    (p.getLastSoldDate().isEqual(startDate) || p.getLastSoldDate().isAfter(startDate)) &&
                    (p.getLastSoldDate().isEqual(endDate) || p.getLastSoldDate().isBefore(endDate))){
                productsWithinRange.add(p);
            }
        }
        //sorting in descending order to get last 5
        productsWithinRange.sort((p1,p2) -> Integer.compare(p2.getTotalSold(),p1.getTotalSold()));

        int limit = Math.min(userLimit,productsWithinRange.size());
        //then sub listing to see last Five products
        return new ArrayList<>(productsWithinRange.subList(0,limit));
    }


    public boolean isUserAdmin(String ID){
        for (User u : userService.getAllUsers()){
            if(ID.equals(u.getID()) && u.getRole().equalsIgnoreCase("admin"))
                return true;
        }
        return false;
    }

}
