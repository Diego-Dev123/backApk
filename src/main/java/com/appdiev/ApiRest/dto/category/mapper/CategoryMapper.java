package com.appdiev.ApiRest.dto.category.mapper;

import com.appdiev.ApiRest.config.APIConfig;
import com.appdiev.ApiRest.dto.category.CategoryRespone;
import com.appdiev.ApiRest.models.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryRespone toCategoryRespone(Category category){
        CategoryRespone respone = new CategoryRespone();

        respone.setId(category.getId());
        respone.setName(category.getName());
        respone.setDescription(category.getDescription());
        respone.setCreatedAt(category.getCreatedAT());
        respone.setUpdatedAt(category.getUpdateAT());
        if(category.getImage() != null){
            String imageUrl = APIConfig.BASE_URL + category.getImage();
            respone.setImage(imageUrl);
        }
        return respone;
    }
}
