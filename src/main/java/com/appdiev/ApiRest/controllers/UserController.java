package com.appdiev.ApiRest.controllers;


import com.appdiev.ApiRest.dto.user.*;
import com.appdiev.ApiRest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


//    @PostMapping //crear user
//    private ResponseEntity<UserResponse> create(@RequestBody CreateUserRequest request) {
//        UserResponse user = userService.create(request);
//        return ResponseEntity.ok(user);
//    }

    @GetMapping("/{id}")
    private ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            UserResponse response = userService.findById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "status", HttpStatus.NOT_FOUND.value()
            ));
        }
    }

//    @PutMapping("upload/{id}")  //quitar el upload
//    public ResponseEntity<?> update(
//            @PathVariable Long id,
//            @ModelAttribute UpdateUserRequest request){
//
//        try {
//            UserResponse response = userService.updateUserImage(id, request);
//            return ResponseEntity.ok(response);
//
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
//                    "message", e.getMessage(),
//                    "status", HttpStatus.INTERNAL_SERVER_ERROR.value()
//            ));
//        }
//    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWithimage(
            @PathVariable Long id,
            @ModelAttribute UpdateUserRequest request) {
        try {
            UserResponse response = userService.updateUserImage(id, request);
            return ResponseEntity.ok(response);

        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", ex.getMessage(),
                    "status", HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "status", HttpStatus.NOT_FOUND.value()
            ));
        }
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) { // 👈 cambia a @RequestBody
        try {
            UserResponse response = userService.update(id, request); // sin imagen
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "status", HttpStatus.NOT_FOUND.value()
            ));
        }
    }




}
