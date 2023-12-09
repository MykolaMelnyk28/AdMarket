package com.api.admarket.api.v1.service;

import com.api.admarket.api.v1.entity.ad.FilterProperties;
import com.api.admarket.api.v1.entity.ad.AdEntity;
import com.api.admarket.api.v1.entity.image.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AdService {

    AdEntity create(AdEntity ad, Long userId);
    Optional<AdEntity> getById(Long adId);
    Page<AdEntity> getAll(Pageable pageable);
    Page<AdEntity> getAllByFilter(FilterProperties filterProperties);
    Page<AdEntity> getAllByUserId(Long sellerId, Pageable pageable);
    Page<AdEntity> getAllSavedAdsByUserId(Long userId, Pageable pageable);
    AdEntity updateById(Long adId, AdEntity ad);
    void deleteById(Long adId);

    String addImage(Long adId, Image image);
    void deleteImage(Long adId, String url);

}
