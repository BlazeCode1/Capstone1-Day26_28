package org.example.capstone1day26.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone1day26.Model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CategoryService {
    ArrayList<Category> categories = new ArrayList<>();
    public ArrayList<Category> getAllCategories(){
        return categories;
    }

    public boolean addCategory(Category category){
        for (Category c : categories){
            if(c.getID().equals(category.getID())){
                return false;// category ID Used
            }
        }
        categories.add(category);
        return true;//  Added Successfully
    }


    public boolean updateCategory(Category category){
        for (Category c : categories){
            if(c.getID().equals(category.getID())){
                categories.set(categories.indexOf(c),category);
                return true; //updated successfully
            }
        }
        return false;// Category ID not found
    }

    public boolean deleteCategory(String ID){
        for (Category c : categories){
            if(ID.equals(c.getID())){
                categories.remove(c);
                return true;
            }
        }
        return false;
    }




}
