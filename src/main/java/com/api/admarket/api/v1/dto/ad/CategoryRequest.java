package com.api.admarket.api.v1.dto.ad;

import com.api.admarket.api.v1.dto.validation.OnCreate;
import com.api.admarket.api.v1.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoryRequest {

    @NotBlank(message = "[category.name] must be 80 characters or less must not be empty", groups = OnCreate.class)
    @Length(min = 3, max = 80, message = "[category.name] must be 80 characters or less", groups = {OnCreate.class})
    @Size(min = 3, max = 80, message = "[category.name] must be 80 characters or less", groups = {OnUpdate.class})
    private String name;

    @Size(min = 3, max = 80, message = "[category.parent] must be 80 characters or less", groups = {OnCreate.class, OnUpdate.class})
    private String parent;

}
