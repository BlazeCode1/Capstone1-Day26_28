package org.example.capstone1day26.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone1day26.Api.ApiResponse;
import org.example.capstone1day26.Model.MerchantStock;
import org.example.capstone1day26.Model.Product;
import org.example.capstone1day26.Service.MerchantStockService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/merchantstock")
@RequiredArgsConstructor
public class MerchantStockController {
    private final MerchantStockService merchantStockService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllMerchantStock(){
        if(merchantStockService.getAllMerchantStocks().isEmpty()){
            return ResponseEntity.badRequest().body(new ApiResponse("Merchant Stock List Empty"));
        }
        return ResponseEntity.ok(merchantStockService.getAllMerchantStocks());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMerchantStock(@Valid @RequestBody MerchantStock merchantStock, Errors err){
        if(err.hasErrors())
            return ResponseEntity.badRequest().body(new ApiResponse(err.getFieldError().getDefaultMessage()));
        int result = merchantStockService.addMerchantStock(merchantStock);

       return switch (result){
            case 0 -> ResponseEntity.badRequest().body(new ApiResponse("Product List Empty"));
            case 1 -> ResponseEntity.badRequest().body(new ApiResponse("Merchant List Empty"));
            case 2 -> ResponseEntity.badRequest().body(new ApiResponse("Invalid Merchant ID"));
            case 3 -> ResponseEntity.badRequest().body(new ApiResponse("Invalid Product ID"));
            case 5 -> ResponseEntity.badRequest().body(new ApiResponse("MerchantStock ID Already Used"));
           default -> ResponseEntity.ok(new ApiResponse("Merchant Stock Added Successfully"));
        };
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateMerchantStock(@Valid @RequestBody MerchantStock merchantStock, Errors err){
        if(err.hasErrors())
            return ResponseEntity.badRequest().body(new ApiResponse(err.getFieldError().getDefaultMessage()));
        int result = merchantStockService.updateMerchantStock(merchantStock);
        return switch (result){
            case 0 -> ResponseEntity.badRequest().body(new ApiResponse("Product List Empty"));
            case 1 -> ResponseEntity.badRequest().body(new ApiResponse("Merchant List Empty"));
            case 2 -> ResponseEntity.badRequest().body(new ApiResponse("Invalid Merchant ID"));
            case 3 -> ResponseEntity.badRequest().body(new ApiResponse("Invalid Product ID"));
            case 4 -> ResponseEntity.badRequest().body(new ApiResponse("Invalid MerchantStock ID"));
            default -> ResponseEntity.ok(new ApiResponse("Merchant Stock Updated Successfully"));
        };
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<?> deleteMerchantStock(@PathVariable String ID){
        if(merchantStockService.deleteMerchantStock(ID)){
            return ResponseEntity.ok(new ApiResponse("Merchant Stock Deleted successfully"));
        }
        return ResponseEntity.badRequest().body(new ApiResponse("Invalid Merchant ID.."));
    }

    @PostMapping("/add/stock/{productID}/{merchantID}/{amount}")
    public ResponseEntity<?> addMoreStock(@PathVariable String productID,@PathVariable String merchantID,@PathVariable int amount){
        int result = merchantStockService.addMoreStock(productID,merchantID,amount);
        return switch (result){
            case 0 -> ResponseEntity.badRequest().body(new ApiResponse("Products List is Empty"));
            case 1 -> ResponseEntity.badRequest().body(new ApiResponse("Merchants List is Empty"));
            case 2 -> ResponseEntity.badRequest().body(new ApiResponse("Invalid Product ID"));
            case 3 -> ResponseEntity.badRequest().body(new ApiResponse("Invalid Merchant ID"));
            case 4 -> ResponseEntity.badRequest().body(new ApiResponse("Merchant Stock not Found"));
            default -> ResponseEntity.ok(new ApiResponse("Added More Stock with amount: "+ amount +" Successfully"));
        };
    }

    @PostMapping("/purchase/{userID}/{productID}/{merchantID}")
    public ResponseEntity<?> userPurchaseProduct(@PathVariable String userID,@PathVariable String productID,@PathVariable String merchantID){
        int result = merchantStockService.userBuyProduct(userID,productID,merchantID);
        return switch (result){
            case 0 -> ResponseEntity.badRequest().body(new ApiResponse("Product List is Empty"));
            case 1 -> ResponseEntity.badRequest().body(new ApiResponse("Merchant List is Empty"));
            case 2 -> ResponseEntity.badRequest().body(new ApiResponse("Invalid Product ID"));
            case 3 -> ResponseEntity.badRequest().body(new ApiResponse("Invalid Merchant ID"));
            case 4 -> ResponseEntity.badRequest().body(new ApiResponse("Invalid User ID"));
            case 5 -> ResponseEntity.badRequest().body(new ApiResponse("Product Is Out Of Stock"));
            case 6  -> ResponseEntity.internalServerError().body(new ApiResponse("(Null Pointer exception)"));
            case 7 -> ResponseEntity.badRequest().body(new ApiResponse("Insufficient Balance"));
            case 9 -> ResponseEntity.badRequest().body(new ApiResponse("Product Out Of Stock"));
            case 12 -> ResponseEntity.badRequest().body(new ApiResponse("Merchant is Suspended"));
            default -> ResponseEntity.ok(new ApiResponse("User Purchased Successfully"));
        };
    }


    @GetMapping("/get/out-of-stock/{merchantID}")
    public ResponseEntity<?> getOutOfStockProductsFromMerchant(@PathVariable String merchantID){
        if(merchantStockService.checkIfMerchantFound(merchantID))
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid Merchant ID"));
        if(merchantStockService.checkIfMerchantHasProductsListed(merchantID))
            return ResponseEntity.badRequest().body(new ApiResponse("Merchant has no products Listed"));
        ArrayList<Product> result = merchantStockService.getOutOfStockProductsForMerchant(merchantID);
        if(result.isEmpty())
            return ResponseEntity.ok().body(new ApiResponse("All Products In Stock"));

        return ResponseEntity.ok(result);


    }

    @PostMapping("/trigger-clearance/{adminID}/{merchantID}")
    public ResponseEntity<?> triggerClearance(@PathVariable String adminID, @PathVariable String merchantID) {
        int result = merchantStockService.triggerClearance(adminID, merchantID);

        return switch (result) {
            case 0 -> ResponseEntity.badRequest().body("Access Denied: Only admins can trigger clearance.");
            case 1 -> ResponseEntity.ok("No eligible products for clearance.");
            case 2 -> ResponseEntity.ok("Clearance campaign triggered successfully.");
            default -> ResponseEntity.internalServerError().body("Unexpected error.");
        };
    }

    @PostMapping("/evaluate-commission-tier/{adminID}/{merchantID}")
    public ResponseEntity<?> evaluateCommissionTier(@PathVariable String adminID,@PathVariable String merchantID){
        String result = merchantStockService.evaluateCommissionTier(adminID,merchantID);
        if(result == null){
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid Merchant ID"));
        }
        if(result.equalsIgnoreCase("Access Denied: Only admins can evaluate Commission Tier."))
            return ResponseEntity.badRequest().body(new ApiResponse(result));

        return ResponseEntity.ok(new ApiResponse(result));
    }

    @PostMapping("/evaluating-merchant-violation/{adminID}/{merchantID}")
    public ResponseEntity<?> evaluatingMerchantViolation(@PathVariable String adminID,@PathVariable String merchantID){
        String result = merchantStockService.evaluatingMerchantViolation(adminID,merchantID);
        return ResponseEntity.ok(new ApiResponse(result));
    }






}

