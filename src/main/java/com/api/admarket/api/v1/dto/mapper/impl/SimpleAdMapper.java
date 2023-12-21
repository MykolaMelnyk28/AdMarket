package com.api.admarket.api.v1.dto.mapper.impl;

import com.api.admarket.api.v1.dto.ad.AdDTO;
import com.api.admarket.api.v1.dto.mapper.AdMapper;
import com.api.admarket.api.v1.dto.mapper.CategoryMapper;
import com.api.admarket.api.v1.dto.user.UserDTO;
import com.api.admarket.api.v1.entity.ad.*;
import com.api.admarket.api.v1.entity.user.UserEntity;
import com.api.admarket.api.v1.service.AdService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class SimpleAdMapper implements AdMapper {

    private final CategoryMapper categoryMapper;
    private final AdService adService;

    @Override
    public AdDTO toDto(AdEntity entity) {
        if (entity == null) {
            return null;
        }

        AdDTO dto = new AdDTO();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setCategory(getCategoryName(entity));
        dto.setSellerUsername(getSellerUsername(entity));
        dto.setPrice(entity.getPrice());
        dto.setCurrencyCode(entity.getCurrency());
        dto.setDescription(entity.getDescription());
        dto.setStatus(getStatusString(entity));
        dto.setItemCondition(getItemConditionString(entity));
        dto.setViewsCount(entity.getViewsCount());
        dto.setDateCreated(entity.getDateCreated());

        dto.setImages(getImages(entity.getId()));

        return dto;
    }

    private String getCategoryName(AdEntity entity) {
        if (entity == null || entity.getCategory() == null || entity.getCategory().getName() == null) {
            return null;
        }
        return entity.getCategory().getName();
    }
    private String getSellerUsername(AdEntity adEntity) {
        if (adEntity == null || adEntity.getUser() == null || adEntity.getUser().getUsername() == null) {
            return null;
        }
        return adEntity.getUser().getUsername();
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

    private List<String> getImages(Long adId) {
        return adService.getImages(adId);
    }

    @Override
    public AdEntity toEntity(AdDTO dto) {
        if (dto == null) {
            return null;
        }

        AdEntity entity = new AdEntity();

        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setCategory(getCategoryEntity(dto));
        entity.setUser(getSellerEntity(dto));
        entity.setPrice(dto.getPrice());
        entity.setCurrency(dto.getCurrencyCode());
        entity.setDescription(dto.getDescription());
        entity.setStatus(getStatusEnum(dto));
        entity.setItemCondition(getItemConditionEnum(dto));
        entity.setViewsCount(dto.getViewsCount());
        entity.setDateCreated(dto.getDateCreated());

        return entity;
    }

    private Category getCategoryEntity(AdDTO dto) {
        if (dto == null || dto.getCategory() == null) {
            return null;
        }
        Category category = new Category();
        category.setName(dto.getCategory());
        return category;
    }

    private UserEntity getSellerEntity(AdDTO dto) {
        if (dto == null || dto.getSellerUsername() == null) {
            return null;
        }
        UserEntity seller = new UserEntity();
        seller.setUsername(dto.getSellerUsername());
        return seller;
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
