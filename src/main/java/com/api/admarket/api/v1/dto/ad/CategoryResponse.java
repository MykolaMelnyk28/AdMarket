package com.api.admarket.api.v1.dto.ad;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CategoryResponse {

    @NotBlank(message = "[name] must not be blank")
    @Length(max = 80, message = "[name] must be 80 characters or less")
    private String name;

    @Valid
    private List<CategoryResponse> children;

}
