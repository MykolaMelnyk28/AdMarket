package com.api.admarket.api.v1.dto.mapper;

import com.api.admarket.api.v1.dto.ad.AdDTO;
import com.api.admarket.api.v1.entity.ad.AdEntity;

public interface AdMapper {
    AdDTO toDto(AdEntity entity);
    AdEntity toEntity(AdDTO dto);
}
