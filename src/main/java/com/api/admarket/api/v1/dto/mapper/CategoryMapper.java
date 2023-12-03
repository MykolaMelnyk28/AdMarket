package com.api.admarket.api.v1.dto.mapper;

import com.api.admarket.api.v1.dto.ad.CategoryRequest;
import com.api.admarket.api.v1.dto.ad.CategoryResponse;
import com.api.admarket.api.v1.entity.ad.Category;

public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

    Category toEntity(CategoryRequest request);

}
