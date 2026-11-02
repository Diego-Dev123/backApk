package com.appdiev.ApiRest.services;


import com.appdiev.ApiRest.dto.product.CreateProductRequest;
import com.appdiev.ApiRest.dto.product.ProductResponse;
import com.appdiev.ApiRest.dto.product.UpdateProductRequest;
import com.appdiev.ApiRest.dto.product.mapper.ProductMapper;
import com.appdiev.ApiRest.models.Category;
import com.appdiev.ApiRest.models.Product;
import com.appdiev.ApiRest.repositories.CategoryRepository;
import com.appdiev.ApiRest.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductMapper productMapper;

    @Transactional
    public ProductResponse create(CreateProductRequest request)throws IOException {
        Category category = categoryRepository.findById(request.getId_category()).orElseThrow(
                () -> new RuntimeException("la categoria no existe")
        );

        Product product = new Product();
        product.setCategory(category);
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        Product savedProduct = productRepository.save(product);
        MultipartFile[] files = request.getFiles();

        if(files != null && files.length > 0){
            String uploadDir = "uploads/products/" + savedProduct.getId();
            Files.createDirectories(Paths.get(uploadDir));

            if (files.length >= 1 && !files[0].isEmpty()){
                String filename1 = files[0].getOriginalFilename();
                String filePath1= Paths.get(uploadDir, filename1).toString();
                Files.copy(files[0].getInputStream(), Paths.get(filePath1), StandardCopyOption.REPLACE_EXISTING);
                savedProduct.setImage1("/"+filePath1.replace("\\","/"));
            }

            if (files.length >= 2 && !files[1].isEmpty()){
                String filename2 = files[1].getOriginalFilename();
                String filePath2= Paths.get(uploadDir, filename2).toString();
                Files.copy(files[1].getInputStream(), Paths.get(filePath2), StandardCopyOption.REPLACE_EXISTING);
                savedProduct.setImage2("/"+filePath2.replace("\\","/"));
            }
            savedProduct = productRepository.save(savedProduct);

        }
        return productMapper.toProductResponse(savedProduct);
    }

    //actualizar
    @Transactional
    public ProductResponse Update(Long id, UpdateProductRequest request)throws IOException {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("el producto no existe")
        );
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        MultipartFile[] files = request.getFiles();

        if(files != null && files.length > 0){
            String uploadDir = "uploads/products/" + product.getId();
            Files.createDirectories(Paths.get(uploadDir));

            if (files.length >= 1 && !files[0].isEmpty()){
                String filename1 = files[0].getOriginalFilename();
                String filePath1= Paths.get(uploadDir, filename1).toString();
                Files.copy(files[0].getInputStream(), Paths.get(filePath1), StandardCopyOption.REPLACE_EXISTING);
                product.setImage1("/"+filePath1.replace("\\","/"));
            }

            if (files.length >= 2 && !files[1].isEmpty()){
                String filename2 = files[1].getOriginalFilename();
                String filePath2= Paths.get(uploadDir, filename2).toString();
                Files.copy(files[1].getInputStream(), Paths.get(filePath2), StandardCopyOption.REPLACE_EXISTING);
                product.setImage2("/"+filePath2.replace("\\","/"));
            }
        }
        Product updateProduct = productRepository.save(product);
        return productMapper.toProductResponse(updateProduct);
    }

    @Transactional
    public List<ProductResponse> findByCategoryId(Long idCategory){
        List<Product> products = productRepository.findByCategoryId(idCategory);
        return products.stream().map(productMapper::toProductResponse).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id){
        Product product = productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("el producto no existe")
        );
        productRepository.delete(product);
    }

}
