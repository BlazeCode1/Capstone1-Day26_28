package org.example.capstone1day26.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone1day26.Api.ApiResponse;
import org.example.capstone1day26.Model.Merchant;
import org.example.capstone1day26.Service.MerchantService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantService merchantService;

    @GetMapping("/get")
    public ResponseEntity<?> getMerchants(){
        if(merchantService.getAllMerchants().isEmpty()){
            return ResponseEntity.badRequest().body(new ApiResponse("Merchants List Is Empty"));
        }
        return ResponseEntity.ok(merchantService.getAllMerchants());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMerchant(@Valid @RequestBody Merchant merchant, Errors err){
        if(err.hasErrors()){
            return ResponseEntity.badRequest().body(err.getFieldError().getDefaultMessage());
        }
        if(merchantService.addMerchant(merchant)){
            return ResponseEntity.ok(new ApiResponse("Merchant Added Successfully"));
        }
        return ResponseEntity.badRequest().body(new ApiResponse("Merchant ID Already Used"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMerchant(@Valid @RequestBody Merchant merchant,Errors err){
        if(err.hasErrors()){
            return ResponseEntity.badRequest().body(err.getFieldError().getDefaultMessage());
        }
        if(merchantService.updateMerchant(merchant)){
            return ResponseEntity.ok(new ApiResponse("Merchant Updated Successfully"));
        }
        return ResponseEntity.badRequest().body(new ApiResponse("Cannot Find Given Category ID"));
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<?> deleteMerchant(@PathVariable String ID){
        if(merchantService.deleteMerchant(ID)){
            return ResponseEntity.ok(new ApiResponse("Merchant Deleted Successfully"));
        }
        return ResponseEntity.badRequest().body(new ApiResponse("Merchant ID Not Found"));

    }

}
