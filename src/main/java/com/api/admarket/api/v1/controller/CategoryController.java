package com.api.admarket.api.v1.controller;

import com.api.admarket.api.v1.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;



}
