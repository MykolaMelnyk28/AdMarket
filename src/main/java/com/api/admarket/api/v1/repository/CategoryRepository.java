package com.api.admarket.api.v1.repository;

import com.api.admarket.api.v1.entity.ad.Category;
import org.apache.el.util.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query(value = """
        SELECT * FROM categories c WHERE c.name = 'all' LIMIT 1
        """, nativeQuery = true)
    Category findRoot();

    Optional<Category> findByName(String name);

    @Modifying
    @Query(value = """
        DELETE FROM categories c
        WHERE c.name = :name
        """, nativeQuery = true)
    void deleteByName(String name);
}
