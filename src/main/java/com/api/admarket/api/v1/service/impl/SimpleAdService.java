package com.api.admarket.api.v1.service.impl;

import com.api.admarket.api.v1.entity.ad.AdEntity;
import com.api.admarket.api.v1.entity.ad.Category;
import com.api.admarket.api.v1.entity.image.Image;
import com.api.admarket.api.v1.entity.user.UserEntity;
import com.api.admarket.api.v1.exeption.CategoryNotLeafException;
import com.api.admarket.api.v1.exeption.ResourceNotFoundException;
import com.api.admarket.api.v1.repository.AdRepository;
import com.api.admarket.api.v1.service.AdService;
import com.api.admarket.api.v1.service.CategoryService;
import com.api.admarket.api.v1.service.ImageService;
import com.api.admarket.api.v1.service.UserService;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SimpleAdService implements AdService {
    private final AdRepository adRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    //private final ImageService imageService;

    public SimpleAdService(
            AdRepository adRepository,
            CategoryService categoryService,
            UserService userService
    ) {
        this.adRepository = adRepository;
        this.categoryService = categoryService;
        this.userService = userService;
        //this.imageService = imageService;
    }

    @Override
    public AdEntity create(AdEntity ad, Long userId) {
        if (ad.getId() != null && getById(ad.getId()).isPresent()) {
            throw new IllegalStateException("User already exists.");
        }
        includeCategory(ad);
        includeUser(ad);
        AdEntity saved = adRepository.save(ad);
        return saved;
    }

    private void includeUser(AdEntity ad) throws ResourceNotFoundException {
        if (ad.getUser() == null) {
            throw new NullPointerException("User can not be null");
        }
        String username = ad.getUser().getUsername();
        if (username == null) {
            throw new NullPointerException("Category name can not be null");
        }
        UserEntity found = userService.getByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("User not found"));
        ad.setUser(found);
    }

    private void includeCategory(AdEntity ad)
            throws ResourceNotFoundException,
            CategoryNotLeafException {
        if (ad.getCategory() == null) {
            throw new NullPointerException("Category can not be null");
        }
        String categoryName = ad.getCategory().getName();
        if (categoryName == null) {
            throw new NullPointerException("Category name can not be null");
        }
        Category found = categoryService.getByName(categoryName).orElseThrow(() ->
                new ResourceNotFoundException("Category not found"));

        if (!found.isLeaf()) {
            throw new CategoryNotLeafException("Category is not a leaf", found);
        }

        ad.setCategory(found);
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
//        String url = imageService.upload(image);
//        adRepository.addImage(userId, url);
        //return url;
        return "";
    }

    @Override
    public void deleteImage(Long userId, String url) {
//        imageService.unload(url);
//        adRepository.deleteImage(userId, url);
    }

    private AdEntity getByIdOrThrow(Long adId) {
        return adRepository.findById(adId).orElseThrow(() ->
                new ResourceNotFoundException("Ad not found")
        );
    }
}
