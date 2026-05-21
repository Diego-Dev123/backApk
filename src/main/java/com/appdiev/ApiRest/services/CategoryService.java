package com.appdiev.ApiRest.services;


import com.appdiev.ApiRest.dto.category.CategoryRespone;
import com.appdiev.ApiRest.dto.category.CreateCategoryRequest;
import com.appdiev.ApiRest.dto.category.mapper.CategoryMapper;
import com.appdiev.ApiRest.models.Category;
import com.appdiev.ApiRest.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    //crear categorias
    @Transactional
    public CategoryRespone create(CreateCategoryRequest request) throws IOException {

        Category category = new Category();

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        Category categorySaved = categoryRepository.save(category);

        if(request.getFile() != null && !request.getFile().isEmpty()){
            String uploadDIr = "uploads/categories/" + categorySaved.getId();
            String fileName = request.getFile().getOriginalFilename();
            String filepath = Paths.get(uploadDIr, fileName).toString();

            Files.createDirectories(Paths.get(uploadDIr));
            Files.copy(request.getFile().getInputStream(), Paths.get(filepath), StandardCopyOption.REPLACE_EXISTING);
            categorySaved.setImage("/" + filepath.replace("\\", "/"));
            categoryRepository.save(categorySaved);
        }
        return categoryMapper.toCategoryRespone(categorySaved);
    }
    //listar categorias
    @Transactional
    public List<CategoryRespone> findAll(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> {
            return categoryMapper.toCategoryRespone(category);
        }).toList();
    }

    //eliminar categorias
    public void delete(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(
            () -> new RuntimeException("La categoria no existe")
        );

        categoryRepository.delete(category);
    }
//
//    //actualizar
//    @Transactional
//    public CategoryRespone update(Long id,CreateCategoryRequest request) throws IOException {
//
//        Category category = categoryRepository.findById(id).orElseThrow(
//                () -> new RuntimeException("La categoria no existe")
//        );
//
//
//        category.setName(request.getName());
//        category.setDescription(request.getDescription());
//
//        if(request.getFile() != null && !request.getFile().isEmpty()){
//
//            //saber si existe una imagen
//            if(category.getImage() != null){
//                Path previusImagePath = Paths.get("." + category.getImage());
//                if(Files.exists(previusImagePath)){
//                    Files.delete(previusImagePath);
//                }
//            }
//
//            String uploadDIr = "uploads/categories/" + category.getId();
//            String fileName = request.getFile().getOriginalFilename();
//            String filepath = Paths.get(uploadDIr, fileName).toString();
//
//            Files.createDirectories(Paths.get(uploadDIr));
//            Files.copy(request.getFile().getInputStream(), Paths.get(filepath), StandardCopyOption.REPLACE_EXISTING);
//            category.setImage("/" + filepath.replace("\\", "/"));
//
//        }
//        Category updatedCategory= categoryRepository.save(category);
//        return categoryMapper.toCategoryRespone(updatedCategory);
//    }





    //claude
    // Solo actualizar datos (sin imagen)
    @Transactional
    public CategoryRespone update(Long id, CreateCategoryRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("La categoria no existe")
        );

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryRespone(updatedCategory);
    }

    // Actualizar con imagen
    @Transactional
    public CategoryRespone updateWithImage(Long id, CreateCategoryRequest request) throws IOException {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("La categoria no existe")
        );

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        if (category.getImage() != null) {
            Path previusImagePath = Paths.get("." + category.getImage());
            if (Files.exists(previusImagePath)) {
                Files.delete(previusImagePath);
            }
        }

        String uploadDir = "uploads/categories/" + category.getId();
        String fileName = request.getFile().getOriginalFilename();
        String filepath = Paths.get(uploadDir, fileName).toString();

        Files.createDirectories(Paths.get(uploadDir));
        Files.copy(request.getFile().getInputStream(), Paths.get(filepath), StandardCopyOption.REPLACE_EXISTING);
        category.setImage("/" + filepath.replace("\\", "/"));

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryRespone(updatedCategory);
    }




}
