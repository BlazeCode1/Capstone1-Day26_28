package org.example.capstone1day26.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone1day26.Api.ApiResponse;
import org.example.capstone1day26.Model.Product;
import org.example.capstone1day26.Service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllProducts() {
        if (productService.getAllProducts().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Product List Empty"));
        }
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product, Errors err) {
        if (err.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse(err.getFieldError().getDefaultMessage()));
        }
        if (productService.addProduct(product) == 0)
            return ResponseEntity.badRequest().body(new ApiResponse("Product ID Already Used"));

        if (productService.addProduct(product) == 1)
            return ResponseEntity.badRequest().body(new ApiResponse("Category List Empty"));

        if (productService.addProduct(product) == 2)
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid Category ID"));

        return ResponseEntity.ok(new ApiResponse("Product Added Successfully"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> // trying to write an actual spring style.:)
    updateProduct(
            @Valid
            @RequestBody
            Product product,
            Errors err
    ) {
        if(err.hasErrors())
            return ResponseEntity.badRequest().body(new ApiResponse(err.getFieldError().getDefaultMessage()));
        int result = productService.updateProduct(product);
        return switch (result) {
            case 0 -> ResponseEntity.badRequest().body(new ApiResponse("Category List Is Empty"));
            case 1 -> ResponseEntity.badRequest().body(new ApiResponse("Category ID Not Found"));
            case 2 -> ResponseEntity.badRequest().body(new ApiResponse("Product ID Not Found"));
            default -> ResponseEntity.ok(new ApiResponse("Product Updated Successfully"));
        };

    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<?> deleteProduct(@PathVariable String ID){
        if(productService.deleteProduct(ID)) {
            return ResponseEntity.ok(new ApiResponse("Product Deleted Successfully"));
        }
        return ResponseEntity.badRequest().body(new ApiResponse("Product ID, Not Found"));
    }

    @GetMapping("/analytics/top-products/{limit}/{start}/{end}")
    public ResponseEntity<?> getTopProductSold(@PathVariable String start,@PathVariable String end,@PathVariable int limit){
        if(productService.getAllProducts().isEmpty())
            return ResponseEntity.badRequest().body(new ApiResponse("Product List Is Empty"));
        ArrayList<Product> result = productService.topFiveProductsPurchased(start,end,limit);
        if(result.isEmpty())
            return ResponseEntity.badRequest().body(new ApiResponse("No Products With Given Interval"));
        return ResponseEntity.ok(result);
    }


}
