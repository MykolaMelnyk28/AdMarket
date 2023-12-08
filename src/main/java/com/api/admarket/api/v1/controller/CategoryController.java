package com.api.admarket.api.v1.controller;

import com.api.admarket.api.v1.dto.ad.CategoryDTO;
import com.api.admarket.api.v1.dto.mapper.CategoryMapper;
import com.api.admarket.api.v1.dto.validation.*;
import com.api.admarket.api.v1.entity.ad.Category;
import com.api.admarket.api.v1.service.CategoryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/categories")
@Validated
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
    public ResponseEntity<CategoryDTO> create(
            @RequestBody @Validated(OnCreate.class) CategoryDTO request
    ) {
        Category categoryRequest = categoryMapper.toEntity(request);
        Category category = categoryService.create(categoryRequest, request.getParent());
        CategoryDTO response = categoryMapper.toDto(category);
        System.out.println(response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{categoryName}")
    public ResponseEntity<CategoryDTO> getByName(
            @PathVariable String categoryName
    ) {
        Optional<Category> categoryOpt = categoryService.getByName(categoryName);
        return ResponseEntity.of(categoryOpt.map(categoryMapper::toDto));
    }

    @PutMapping("/{categoryName}")
    public ResponseEntity<CategoryDTO> updateByName(
            @PathVariable String categoryName,
            @RequestBody @Validated(OnUpdate.class) CategoryDTO request
    ) {
        Category categoryRequest = categoryMapper.toEntity(request);
        Category category = categoryService.updateByName(categoryName, categoryRequest);
        CategoryDTO response = categoryMapper.toDto(category);
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
