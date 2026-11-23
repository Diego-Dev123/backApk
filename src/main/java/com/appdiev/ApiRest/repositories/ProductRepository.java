package com.appdiev.ApiRest.repositories;

import com.appdiev.ApiRest.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long idCategory);

    /** Productos activos cuya categoría también está activa. */
    List<Product> findByCategoryIdAndDeletedAtIsNullAndCategoryDeletedAtIsNull(Long idCategory);

    Optional<Product> findByIdAndDeletedAtIsNull(Long id);

    Optional<Product> findByIdAndDeletedAtIsNotNull(Long id);

    List<Product> findByDeletedAtIsNotNullOrderByDeletedAtDesc();
}
