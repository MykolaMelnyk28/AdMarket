package com.api.admarket.api.v1.entity.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserImage {

    private MultipartFile file;

}
