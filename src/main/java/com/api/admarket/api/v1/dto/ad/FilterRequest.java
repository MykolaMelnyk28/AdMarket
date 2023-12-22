package com.api.admarket.api.v1.dto.ad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {

    private int page = 0;

    private int limit = 10;

    private String category = "all";

    private String title = "";

    private String sort = "title asc";

}
