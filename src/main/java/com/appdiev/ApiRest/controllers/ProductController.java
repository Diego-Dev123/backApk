package com.appdiev.ApiRest.controllers;


import com.appdiev.ApiRest.dto.category.CategoryRespone;
import com.appdiev.ApiRest.dto.category.CreateCategoryRequest;
import com.appdiev.ApiRest.dto.product.CreateProductRequest;
import com.appdiev.ApiRest.dto.product.ProductResponse;
import com.appdiev.ApiRest.dto.product.UpdateProductRequest;
import com.appdiev.ApiRest.exceptions.ConflictException;
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
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "statusCode", HttpStatus.CONFLICT.value(),
                    "message", e.getMessage()
            ));
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
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @ModelAttribute UpdateProductRequest request) {

        try {
            ProductResponse response = productService.Update(id, request);
            return ResponseEntity.ok(response);

        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "statusCode", HttpStatus.CONFLICT.value(),
                    "message", e.getMessage()
            ));
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
    @PutMapping("upload/{id}")
    public ResponseEntity<?> updateWithImage(
            @PathVariable Long id,
            @ModelAttribute UpdateProductRequest request){
        try {
            ProductResponse response = productService.Update(id, request);
            return ResponseEntity.ok(response);
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "statusCode", HttpStatus.CONFLICT.value(),
                    "message", e.getMessage()
            ));
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
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "statusCode", HttpStatus.CONFLICT.value(),
                    "message", e.getMessage()
            ));
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
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "statusCode", HttpStatus.CONFLICT.value(),
                    "message", e.getMessage()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "status", HttpStatus.NOT_FOUND.value()
            ));
        }
    }

    //papelera
    @GetMapping("/trash")
    public ResponseEntity<?> trash(){
        return ResponseEntity.ok(productService.findTrash());
    }

    //restaurar
    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restore(@PathVariable Long id){
        try {
            return ResponseEntity.ok(productService.restore(id));
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "statusCode", HttpStatus.CONFLICT.value(),
                    "message", e.getMessage()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "statusCode", HttpStatus.NOT_FOUND.value(),
                    "message", e.getMessage()
            ));
        }
    }

}
