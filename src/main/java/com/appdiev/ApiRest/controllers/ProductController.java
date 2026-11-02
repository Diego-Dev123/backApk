package com.appdiev.ApiRest.controllers;


import com.appdiev.ApiRest.dto.category.CategoryRespone;
import com.appdiev.ApiRest.dto.category.CreateCategoryRequest;
import com.appdiev.ApiRest.dto.product.CreateProductRequest;
import com.appdiev.ApiRest.dto.product.ProductResponse;
import com.appdiev.ApiRest.dto.product.UpdateProductRequest;
import com.appdiev.ApiRest.models.Product;
import com.appdiev.ApiRest.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    //crear
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@ModelAttribute CreateProductRequest request){
        try {
            ProductResponse response = productService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
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

    //update
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @ModelAttribute UpdateProductRequest request){
        try {
            ProductResponse response = productService.Update(id, request);
            return ResponseEntity.ok(response);
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


    //create
    @GetMapping("/category/{idCategory}")
    public ResponseEntity<?> finByCategoryId(@PathVariable Long idCategory){
        try {
            List<ProductResponse> response = productService.findByCategoryId(idCategory);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "status", HttpStatus.NOT_FOUND.value()
            ));
        }
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try {
            productService.delete(id);
            return ResponseEntity.ok(true);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "status", HttpStatus.NOT_FOUND.value()
            ));
        }
    }


}
