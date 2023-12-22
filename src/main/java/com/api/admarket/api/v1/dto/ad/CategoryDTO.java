package com.api.admarket.api.v1.dto.ad;

import com.api.admarket.api.v1.dto.validation.OnCreate;
import com.api.admarket.api.v1.dto.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDTO {

    @NotBlank(message = "Category name can not be empty.",
            groups = OnCreate.class)
    @Size(min = 3, max = 80,
            message = "Category name must be 80 characters or less",
            groups = OnCreate.class)
    private String name;

    @Size(min = 3, max = 80, message = "Category name must be 80 characters or less", groups = {OnCreate.class, OnUpdate.class})
    private String parent;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<CategoryDTO> children = new ArrayList<>();

}
