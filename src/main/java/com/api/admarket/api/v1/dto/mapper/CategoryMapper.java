package com.api.admarket.api.v1.dto.mapper;

import com.api.admarket.api.v1.dto.ad.CategoryDTO;
import com.api.admarket.api.v1.entity.ad.Category;

public interface CategoryMapper {

    CategoryDTO toDto(Category dto);
    Category toEntity(CategoryDTO category);

}
