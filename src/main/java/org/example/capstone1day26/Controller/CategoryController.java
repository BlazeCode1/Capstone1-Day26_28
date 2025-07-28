package org.example.capstone1day26.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone1day26.Api.ApiResponse;
import org.example.capstone1day26.Model.Category;
import org.example.capstone1day26.Service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/get")
    public ResponseEntity<?> getCategories() {
        if (categoryService.getAllCategories().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Category List Is Empty"));
        }
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@Valid @RequestBody Category category, Errors err){
        if(err.hasErrors()){
            return ResponseEntity.badRequest().body(err.getFieldError().getDefaultMessage());
        }
        if(categoryService.addCategory(category)){
            return ResponseEntity.ok(new ApiResponse("Category Added Successfully"));
        }
        return ResponseEntity.badRequest().body(new ApiResponse("Category ID Already used"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCategory(@Valid @RequestBody Category category,Errors err){
        if(err.hasErrors()){
            return ResponseEntity.badRequest().body(err.getFieldError().getDefaultMessage());
        }
        if(categoryService.updateCategory(category)){
            return ResponseEntity.ok(new ApiResponse("Category Updated Successfully"));
        }
        return ResponseEntity.badRequest().body(new ApiResponse("Category ID Not Found"));
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<?> deleteCategory(@PathVariable String ID){
        if(categoryService.deleteCategory(ID)){
            return ResponseEntity.ok(new ApiResponse("Category Deleted Successfully"));
        }
        return ResponseEntity.badRequest().body(new ApiResponse("Category With Given ID not Found"));
    }


}
