package com.api.admarket.api.v1.dto.mapper.impl;

import com.api.admarket.api.v1.dto.ad.AdDTO;
import com.api.admarket.api.v1.dto.mapper.AdMapper;
import com.api.admarket.api.v1.dto.mapper.CategoryMapper;
import com.api.admarket.api.v1.entity.ad.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SimpleAdMapper implements AdMapper {

    private final CategoryMapper categoryMapper;

    @Override
    public AdDTO toDto(AdEntity entity) {
        if (entity == null) {
            return null;
        }

        AdDTO dto = new AdDTO();

        dto.setTitle(entity.getTitle());
        dto.setCategory(categoryMapper.toDto(entity.getCategory()));
        dto.setPrice(entity.getPrice());
        dto.setCurrencyCode(entity.getCurrency());
        dto.setDescription(entity.getDescription());
        dto.setStatus(getStatusString(entity));
        dto.setItemCondition(getItemConditionString(entity));
        dto.setViewsCount(entity.getViewsCount());
        dto.setDateCreated(entity.getDateCreated());

        return dto;
    }

    private String getStatusString(AdEntity entity) {
        if (entity == null || entity.getStatus() == null) {
            return null;
        }
        return entity.getStatus().name().toLowerCase();
    }

    private String getItemConditionString(AdEntity entity) {
        if (entity == null || entity.getItemCondition() == null) {
            return null;
        }
        return entity.getItemCondition().name().toLowerCase();
    }

    @Override
    public AdEntity toEntity(AdDTO dto) {
        if (dto == null) {
            return null;
        }

        AdEntity entity = new AdEntity();

        entity.setTitle(entity.getTitle());
        entity.setCategory(categoryMapper.toEntity(dto.getCategory()));
        entity.setPrice(entity.getPrice());
        entity.setCurrency(entity.getCurrency());
        entity.setDescription(entity.getDescription());
        entity.setStatus(getStatusEnum(dto));
        entity.setItemCondition(getItemConditionEnum(dto));
        entity.setViewsCount(entity.getViewsCount());
        entity.setDateCreated(entity.getDateCreated());

        return entity;
    }

    private AdStatus getStatusEnum(AdDTO dto) {
        if (dto == null || dto.getStatus() == null) {
            return null;
        }
        String enumName = dto.getStatus().toUpperCase();
        return AdStatus.valueOf(enumName);
    }

    private ItemCondition getItemConditionEnum(AdDTO dto) {
        if (dto == null || dto.getItemCondition() == null) {
            return null;
        }
        String enumName = dto.getItemCondition().toUpperCase();
        return ItemCondition.valueOf(enumName);
    }
}
