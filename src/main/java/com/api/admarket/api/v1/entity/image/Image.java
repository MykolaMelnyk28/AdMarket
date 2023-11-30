package com.api.admarket.api.v1.entity.image;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class Image {

    private MultipartFile file;

}
