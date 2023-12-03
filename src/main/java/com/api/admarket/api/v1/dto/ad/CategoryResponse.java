package com.api.admarket.api.v1.dto.ad;

import lombok.Data;

import java.util.List;

@Data
public class CategoryResponse {

    private String name;
    private List<CategoryResponse> children;

}
