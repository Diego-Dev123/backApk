package com.appdiev.ApiRest.controllers;


import com.appdiev.ApiRest.dto.user.CreateUserRequest;
import com.appdiev.ApiRest.dto.user.UserResponse;
import com.appdiev.ApiRest.dto.user.LoginRequest;
import com.appdiev.ApiRest.dto.user.LoginResponse;
import com.appdiev.ApiRest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;


    @PostMapping("/register") //crear user
    private ResponseEntity<?> create(@RequestBody CreateUserRequest request){
        try {
            LoginResponse user = userService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", e.getMessage(),
                    "status", HttpStatus.BAD_REQUEST.value()
            ));
        }
    }


    @PostMapping("/login")
    private ResponseEntity<?> login (@RequestBody LoginRequest request){
        try {
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "message", e.getMessage(),
                    "status", HttpStatus.UNAUTHORIZED.value()
            ));
        }
    }
}
