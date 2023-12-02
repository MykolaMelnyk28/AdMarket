package com.api.admarket.api.v1.service.impl;

import com.api.admarket.api.v1.entity.ad.AdEntity;
import com.api.admarket.api.v1.entity.image.Image;
import com.api.admarket.api.v1.exeption.ResourceNotFoundException;
import com.api.admarket.api.v1.repository.AdRepository;
import com.api.admarket.api.v1.service.AdService;
import com.api.admarket.api.v1.service.CategoryService;
import com.api.admarket.api.v1.service.ImageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

//@Service
public class SimpleAdService implements AdService {
    private final AdRepository adRepository;
    private final ImageService imageService;

    public SimpleAdService(AdRepository adRepository, ImageService imageService) {
        this.adRepository = adRepository;
        this.imageService = imageService;
    }

    @Override
    public AdEntity create(AdEntity ad, Long userId) {
        if (ad.getId() != null && getById(ad.getId()).isPresent()) {
            throw new IllegalStateException("User already exists.");
        }
        AdEntity saved = adRepository.save(ad);
        adRepository.assignUser(userId, ad.getId());
        return saved;
    }

    @Override
    public Optional<AdEntity> getById(Long adId) {
        return adRepository.findById(adId);
    }

    @Override
    public Page<AdEntity> getAll(Pageable pageable) {
        return adRepository.findAll(pageable);
    }

    @Override
    public Page<AdEntity> getAllByPartialTitle(String categoryName, String title, Pageable pageable) {
        return adRepository.findAllByTitleContains(categoryName, title, pageable);
    }

    @Override
    public Page<AdEntity> getAllByUserId(Long sellerId, Pageable pageable) {
        return adRepository.findAllByUserId(sellerId, pageable);
    }

    @Override
    public Page<AdEntity> getAllSavedAdsByUserId(Long userId, Pageable pageable) {
        return adRepository.findSavedAdsByUserId(userId, pageable);
    }

    @Override
    public AdEntity updateById(Long adId, AdEntity ad) {
        AdEntity found = getByIdOrThrow(adId);
        copy(ad, found);
        return found;
    }

    private void copy(AdEntity source, AdEntity destination) {
        destination.setTitle(source.getTitle());
        destination.setCategory(source.getCategory());
        destination.setPrice(source.getPrice());
        destination.setCurrency(source.getCurrency());
        destination.setDescription(source.getDescription());
        destination.setStatus(source.getStatus());
        destination.setItemCondition(source.getItemCondition());
        destination.setUser(source.getUser());
        destination.setViewsCount(source.getViewsCount());
        destination.setImageUrls(source.getImageUrls());
    }

    @Override
    public void deleteById(Long adId) {
        adRepository.deleteById(adId);
    }

    @Override
    public String addImage(Long userId, Image image) {
        String url = imageService.upload(image);
        adRepository.addImage(userId, url);
        return url;
    }

    @Override
    public void deleteImage(Long userId, String url) {
        imageService.unload(url);
        adRepository.deleteImage(userId, url);
    }

    private AdEntity getByIdOrThrow(Long adId) {
        return adRepository.findById(adId).orElseThrow(() ->
                new ResourceNotFoundException("Ad not found")
        );
    }
}
