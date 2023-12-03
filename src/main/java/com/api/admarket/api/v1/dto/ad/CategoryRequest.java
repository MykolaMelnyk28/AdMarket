package com.api.admarket.api.v1.dto.ad;

import lombok.Data;

@Data
public class CategoryRequest {

    private String name;
    private String parent;

}
