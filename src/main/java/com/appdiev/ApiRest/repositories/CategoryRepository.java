package com.appdiev.ApiRest.repositories;

import com.appdiev.ApiRest.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
