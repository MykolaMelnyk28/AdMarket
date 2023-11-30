package com.api.admarket.api.v1.repository;

import com.api.admarket.api.v1.entity.ad.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

}