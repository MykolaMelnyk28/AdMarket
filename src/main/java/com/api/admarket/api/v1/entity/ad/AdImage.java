package com.api.admarket.api.v1.entity.ad;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AdImage {

    private MultipartFile file;

}
