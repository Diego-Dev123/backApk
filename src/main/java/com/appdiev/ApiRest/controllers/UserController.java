package com.appdiev.ApiRest.controllers;


import com.appdiev.ApiRest.dto.user.CreateUserRequest;
import com.appdiev.ApiRest.dto.user.CreateUserResponse;
import com.appdiev.ApiRest.dto.user.LoginRequest;
import com.appdiev.ApiRest.dto.user.LoginResponse;
import com.appdiev.ApiRest.models.User;
import com.appdiev.ApiRest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping //crear user
     private ResponseEntity<CreateUserResponse> create(@RequestBody CreateUserRequest request){
        CreateUserResponse user = userService.create(request);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> findById(@PathVariable Long id){
        try {
            CreateUserResponse response = userService.findById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "status", HttpStatus.NOT_FOUND.value()
            ));
        }
    }
}
