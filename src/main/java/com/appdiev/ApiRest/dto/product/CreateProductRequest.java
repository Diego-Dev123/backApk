package com.appdiev.ApiRest.dto.product;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateProductRequest {

    private Long id_category;
    private String name;
    private String description;
    private Double price;
    private MultipartFile[] files;
}
