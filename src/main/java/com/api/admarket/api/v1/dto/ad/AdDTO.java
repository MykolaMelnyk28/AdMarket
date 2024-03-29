package com.api.admarket.api.v1.dto.ad;

import com.api.admarket.api.v1.dto.validation.OnCreate;
import com.api.admarket.api.v1.dto.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class AdDTO {

    @Min(value = 1,
            message = "Id can not be negative.",
            groups = {OnCreate.class, OnUpdate.class})
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Title can not be empty.",
            groups = {OnCreate.class})
    @Size(max = 100,
            message = "Title must be 20 characters or less.",
            groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @NotBlank(message = "Category name can not be empty.",
            groups = OnCreate.class)
    @Size(min = 3, max = 80,
            message = "Category name must be 80 characters or less",
            groups = OnCreate.class)
    private String category;

    @NotNull(message = "Price can not be empty.",
            groups = OnCreate.class)
    @PositiveOrZero(message = "Price is can not be negative.",
                    groups = {OnCreate.class, OnUpdate.class})
    private BigDecimal price = null;

    @NotBlank(message = "Currency code can not be empty",
              groups = OnCreate.class)
    @Size(min = 3, max = 3,
            message = "Currency code must be 3 characters.",
            groups = {OnCreate.class, OnUpdate.class})
    private String currencyCode;

    private String description;

    @NotBlank(message = "Status can not be empty.",
            groups = OnCreate.class)
    @Pattern(regexp = "^(active|inactive|sold)$",
            message = "Incorrect status value.",
            groups = {OnCreate.class, OnUpdate.class})
    private String status = "active";

    @NotBlank(message = "Item condition can not be empty.",
            groups = OnCreate.class)
    @Pattern(regexp = "^(new|none|used)$",
            message = "Incorrect item condition value.",
            groups = {OnCreate.class, OnUpdate.class})
    private String itemCondition = "new";

    @NotBlank(message = "Username can not be empty.",
            groups = OnCreate.class)
    @Size(max = 20,
            message = "Seller username must be 20 characters or less.",
            groups = {OnCreate.class, OnUpdate.class})
    private String sellerUsername;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, defaultValue = "0")
    private int viewsCount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dateCreated;

    private List<String> images = new ArrayList<>();

}
