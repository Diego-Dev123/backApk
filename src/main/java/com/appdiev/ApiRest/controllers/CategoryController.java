package com.appdiev.ApiRest.controllers;

import com.appdiev.ApiRest.dto.category.CategoryRespone;
import com.appdiev.ApiRest.dto.category.CreateCategoryRequest;
import com.appdiev.ApiRest.dto.user.UserResponse;
import com.appdiev.ApiRest.models.Category;
import com.appdiev.ApiRest.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //crear
    @PostMapping
    public ResponseEntity<?> create(@ModelAttribute CreateCategoryRequest request){
        try {
            CategoryRespone category = categoryService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "status", HttpStatus.NOT_FOUND.value()
            ));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", e.getMessage(),
                    "status", HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
        }
    }
    //listar
    @GetMapping
    public ResponseEntity<?> findAll(){
        try {
            List<CategoryRespone> categories = categoryService.findAll();
            return ResponseEntity.ok(categories);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", e.getMessage(),
                    "status", HttpStatus.BAD_REQUEST.value()
            ));
        }
    }

    //elliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try {
            categoryService.delete(id);
            return ResponseEntity.ok(true);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "status", HttpStatus.NOT_FOUND.value()
            ));
        }
    }

    //actualizar
    @PutMapping("/upload/{id}")
    public ResponseEntity<?> updateWithImage(@PathVariable Long id, @ModelAttribute CreateCategoryRequest request){
        try {
            CategoryRespone respone = categoryService.updateWithImage(id, request);
            return ResponseEntity.ok(respone);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "status", HttpStatus.NOT_FOUND.value()
            ));
        } catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", e.getMessage(),
                    "status", HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
        }
    }
    //sin imagen
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @ModelAttribute CreateCategoryRequest request) { // 👈 solo esto
        try {
            CategoryRespone respone = categoryService.update(id, request);
            return ResponseEntity.ok(respone);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "status", HttpStatus.NOT_FOUND.value()
            ));
        }
    }






}
