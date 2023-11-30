package com.api.admarket.api.v1.service;

import com.api.admarket.api.v1.entity.image.Image;

public interface ImageService {

    String upload(Image image);
    void unload(String url);

}
