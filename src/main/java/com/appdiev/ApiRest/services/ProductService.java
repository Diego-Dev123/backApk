package com.appdiev.ApiRest.services;

import com.appdiev.ApiRest.dto.product.CreateProductRequest;
import com.appdiev.ApiRest.dto.product.ProductResponse;
import com.appdiev.ApiRest.dto.product.UpdateProductRequest;
import com.appdiev.ApiRest.dto.product.mapper.ProductMapper;
import com.appdiev.ApiRest.exceptions.ConflictException;
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
import java.time.LocalDateTime;
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
        if (category.getDeletedAt() != null) {
            throw new ConflictException("No se puede crear el producto porque su categoría está eliminada.");
        }

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

//    //actualizar
//
//
//
//

    @Transactional
    public ProductResponse Update(Long id, UpdateProductRequest request) throws IOException {

        Product product = productRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new RuntimeException("el producto no existe")
        );
        if (product.getCategory().getDeletedAt() != null) {
            throw new ConflictException("No se puede editar el producto porque su categoría está eliminada.");
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        MultipartFile[] files = request.getFiles();
        Integer[] imageToUpdate = request.getImageToUpdate();

        if (files != null && imageToUpdate != null) {

            String uploadDir = "uploads/products/" + product.getId();
            Files.createDirectories(Paths.get(uploadDir));

            for (int i = 0; i < files.length && i < imageToUpdate.length; i++) {

                if (files[i] == null || files[i].isEmpty()) {
                    continue;
                }

                String filename = files[i].getOriginalFilename();
                String filePath = Paths.get(uploadDir, filename).toString();

                Files.copy(
                        files[i].getInputStream(),
                        Paths.get(filePath),
                        StandardCopyOption.REPLACE_EXISTING
                );

                String imagePath = "/" + filePath.replace("\\", "/");

                if (imageToUpdate[i] == 1) {
                    product.setImage1(imagePath);
                } else if (imageToUpdate[i] == 2) {
                    product.setImage2(imagePath);
                }
            }
        }

        Product updateProduct = productRepository.save(product);

        return productMapper.toProductResponse(updateProduct);
    }

    @Transactional
    public List<ProductResponse> findByCategoryId(Long idCategory){
        List<Product> products = productRepository.findByCategoryIdAndDeletedAtIsNullAndCategoryDeletedAtIsNull(idCategory);
        return products.stream().map(productMapper::toProductResponse).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id){
        Product product = productRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new RuntimeException("el producto no existe")
        );
        // borrado lógico: no se elimina el registro ni las imágenes
        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findTrash() {
        return productRepository.findByDeletedAtIsNotNullOrderByDeletedAtDesc()
                .stream().map(productMapper::toProductResponse).collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse restore(Long id) {
        Product product = productRepository.findByIdAndDeletedAtIsNotNull(id).orElseThrow(
                () -> new RuntimeException("el producto no existe en la papelera")
        );
        if (product.getCategory().getDeletedAt() != null) {
            throw new ConflictException("No se puede restaurar el producto porque su categoría está eliminada.");
        }
        product.setDeletedAt(null);
        return productMapper.toProductResponse(productRepository.save(product));
    }

}
