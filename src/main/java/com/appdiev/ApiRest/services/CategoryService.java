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
import java.time.LocalDateTime;
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
        List<Category> categories = categoryRepository.findByDeletedAtIsNull();
        return categories.stream().map(category -> {
            return categoryMapper.toCategoryRespone(category);
        }).toList();
    }

    //eliminar categorias
    @Transactional
    public void delete(Long id){
        Category category = categoryRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
            () -> new RuntimeException("La categoria no existe")
        );

        // borrado lógico: los productos quedan ocultos por la relación, sin marcarse
        category.setDeletedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryRespone> findTrash(){
        return categoryRepository.findByDeletedAtIsNotNullOrderByDeletedAtDesc()
                .stream().map(categoryMapper::toCategoryRespone).toList();
    }

    @Transactional
    public CategoryRespone restore(Long id){
        Category category = categoryRepository.findByIdAndDeletedAtIsNotNull(id).orElseThrow(
            () -> new RuntimeException("La categoria no existe en la papelera")
        );
        category.setDeletedAt(null);
        return categoryMapper.toCategoryRespone(categoryRepository.save(category));
    }
//
//    //actualizar
//
//
//
//
//
//            //saber si existe una imagen
//
//
//

    //claude
    @Transactional
    public CategoryRespone update(Long id, CreateCategoryRequest request) {
        Category category = categoryRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
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
        Category category = categoryRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
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
