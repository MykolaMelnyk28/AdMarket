package com.api.admarket.api.v1.dto.mapper.impl;

import com.api.admarket.api.v1.dto.mapper.FiltersMapper;
import com.api.admarket.api.v1.entity.ad.FilterProperties;
import com.api.admarket.api.v1.dto.ad.FilterRequest;
import org.springframework.stereotype.Component;

@Component
public class SimpleFilterMapper implements FiltersMapper {

    @Override
    public FilterRequest toDto(FilterProperties entity) {
        if (entity == null) {
            return null;
        }

        FilterRequest dto = new FilterRequest();
        dto.setPage(entity.getPage());
        dto.setLimit(entity.getLimit());
        dto.setTitle(entity.getTitle());

        return dto;
    }

    @Override
    public FilterProperties toEntity(FilterRequest dto) {
        if (dto == null) {
            return null;
        }

        FilterProperties entity = new FilterProperties();

        entity.setPage(dto.getPage());
        entity.setLimit(dto.getLimit());
        entity.setTitle(dto.getTitle());

        return entity;
    }

}
