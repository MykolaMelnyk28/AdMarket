package com.api.admarket.api.v1.repository;

import com.api.admarket.api.v1.entity.ad.Category;
import org.apache.el.util.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query(value = """
        UPDATE categories c
        SET c.parent_name = :categoryParentName
        WHERE c.name = :categoryName
        """, nativeQuery = true)
    void assignParent(String categoryName, String categoryParentName);
}
