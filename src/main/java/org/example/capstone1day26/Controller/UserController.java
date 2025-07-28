package org.example.capstone1day26.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone1day26.Api.ApiResponse;
import org.example.capstone1day26.Model.User;
import org.example.capstone1day26.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getUsers(){
        if(userService.getAllUsers().isEmpty()){
            return ResponseEntity.badRequest().body(new ApiResponse("Users List Is Empty"));
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user, Errors err){
        if(err.hasErrors()){
            return ResponseEntity.badRequest().body(err.getFieldError().getDefaultMessage());
        }
        if(userService.addUser(user)){
            return ResponseEntity.ok(new ApiResponse("User Added Successfully"));
        }
        return ResponseEntity.badRequest().body(new ApiResponse("User ID Already Used"));
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user, Errors err){
        if(err.hasErrors()){
            return ResponseEntity.badRequest().body(err.getFieldError().getDefaultMessage());
        }
        if(userService.updateUser(user)){
            return ResponseEntity.ok(new ApiResponse("User Updated Successfully"));
        }
        return ResponseEntity.badRequest().body(new ApiResponse("Category ID Not Found"));
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<?> deleteUser(@PathVariable String ID){
        if(userService.deleteUser(ID)){
            return ResponseEntity.ok(new ApiResponse("User Deleted Successfully"));
        }
        return ResponseEntity.badRequest().body(new ApiResponse("User ID Not Found"));

    }
}
