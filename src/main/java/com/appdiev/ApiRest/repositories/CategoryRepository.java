package com.appdiev.ApiRest.repositories;

import com.appdiev.ApiRest.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByDeletedAtIsNull();

    List<Category> findByDeletedAtIsNotNullOrderByDeletedAtDesc();

    Optional<Category> findByIdAndDeletedAtIsNull(Long id);

    Optional<Category> findByIdAndDeletedAtIsNotNull(Long id);
}
