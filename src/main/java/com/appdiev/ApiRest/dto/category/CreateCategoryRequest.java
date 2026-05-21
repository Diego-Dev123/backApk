package com.appdiev.ApiRest.dto.category;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class CreateCategoryRequest {

    private String name;
    private String description;
    private MultipartFile file;
}
