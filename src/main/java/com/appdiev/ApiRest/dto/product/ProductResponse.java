package com.appdiev.ApiRest.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductResponse {

    private Long id;
    @JsonProperty("id_category")
    private Long idCategory;
    private String name;
    private String description;
    private Double price;
    private String image1;
    private String image2;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

}
