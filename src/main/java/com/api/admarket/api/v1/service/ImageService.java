package com.api.admarket.api.v1.service;

import com.api.admarket.api.v1.entity.image.Image;

import java.util.List;

public interface ImageService {

    void upload(String filename, Image image);
    String getUrl(String filename, int expiryDays);
    List<String> getUrls(String directory, int expiryDays);
    void delete(String filename);

}
