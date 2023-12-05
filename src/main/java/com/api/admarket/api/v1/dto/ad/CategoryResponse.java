package com.api.admarket.api.v1.dto.ad;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CategoryResponse {

    private String name;
    private List<CategoryResponse> children;

}
