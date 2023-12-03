package com.api.admarket.api.v1.controller;

import com.api.admarket.api.v1.dto.ad.CategoryRequest;
import com.api.admarket.api.v1.dto.ad.CategoryResponse;
import com.api.admarket.api.v1.dto.mapper.CategoryMapper;
import com.api.admarket.api.v1.entity.ad.Category;
import com.api.admarket.api.v1.service.CategoryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;


    public CategoryController(
            CategoryService categoryService,
            @Qualifier("simple")
            CategoryMapper categoryMapper
    ) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @RequestBody CategoryRequest request
    ) {
        Category categoryRequest = categoryMapper.toEntity(request);
        Category category = categoryService.create(categoryRequest, request.getParent());
        CategoryResponse response = categoryMapper.toResponse(category);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{categoryName}")
    public ResponseEntity<CategoryResponse> getByName(
            @PathVariable String categoryName
    ) {
        Optional<Category> categoryOpt = categoryService.getByName(categoryName);
        return ResponseEntity.of(categoryOpt.map(categoryMapper::toResponse));
    }

    @PutMapping("/{categoryName}")
    public ResponseEntity<CategoryResponse> updateByName(
            @PathVariable String categoryName,
            @RequestBody CategoryRequest request
    ) {
        Category categoryRequest = categoryMapper.toEntity(request);
        System.out.println(categoryRequest);
        Category category = categoryService.updateByName(categoryName, categoryRequest);
        CategoryResponse response = categoryMapper.toResponse(category);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{categoryName}")
    public HttpStatus deleteById(
            @PathVariable String categoryName
    ) {
        categoryService.deleteByName(categoryName);
        return HttpStatus.OK;
    }
}
