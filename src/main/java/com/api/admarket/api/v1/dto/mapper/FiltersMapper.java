package com.api.admarket.api.v1.dto.mapper;

import com.api.admarket.api.v1.entity.ad.FilterProperties;
import com.api.admarket.api.v1.dto.ad.FilterRequest;

public interface FiltersMapper {

    FilterRequest toDto(FilterProperties entity);
    FilterProperties toEntity(FilterRequest dto);

}
