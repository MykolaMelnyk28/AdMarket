package com.api.admarket.api.v1.controller;

import com.api.admarket.api.v1.dto.ad.AdDTO;
import com.api.admarket.api.v1.dto.ad.CategoryDTO;
import com.api.admarket.api.v1.dto.mapper.AdMapper;
import com.api.admarket.api.v1.dto.mapper.CategoryMapper;
import com.api.admarket.api.v1.dto.mapper.FiltersMapper;
import com.api.admarket.api.v1.dto.validation.*;
import com.api.admarket.api.v1.entity.ad.FilterProperties;
import com.api.admarket.api.v1.entity.ad.AdEntity;
import com.api.admarket.api.v1.entity.ad.Category;
import com.api.admarket.api.v1.dto.ad.FilterRequest;
import com.api.admarket.api.v1.service.AdService;
import com.api.admarket.api.v1.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;
    private final AdService adService;
    private final AdMapper adMapper;
    private final FiltersMapper filtersMapper;

    @Qualifier("simple")
    private final CategoryMapper categoryMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> create(
            @RequestBody @Validated(OnCreate.class) CategoryDTO request
    ) {
        Category categoryRequest = categoryMapper.toEntity(request);
        Category category = categoryService.create(categoryRequest, request.getParent());
        CategoryDTO response = categoryMapper.toDto(category);
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public HttpStatus deleteById(
            @PathVariable String categoryName
    ) {
        categoryService.deleteByName(categoryName);
        return HttpStatus.OK;
    }
}
