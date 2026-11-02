package com.appdiev.ApiRest.dto.product.mapper;


import com.appdiev.ApiRest.config.APIConfig;
import com.appdiev.ApiRest.dto.product.ProductResponse;
import com.appdiev.ApiRest.models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {


    public ProductResponse toProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setCreatedAt(product.getCreatedAT());
        response.setUpdatedAt(product.getUpdateAT());
        response.setIdCategory(product.getCategory().getId());

        if (product.getImage1() != null){
            response.setImage1(APIConfig.BASE_URL + product.getImage1());
        }

        if (product.getImage2() != null){
            response.setImage2(APIConfig.BASE_URL + product.getImage2());
        }

        return response;
    }
}
