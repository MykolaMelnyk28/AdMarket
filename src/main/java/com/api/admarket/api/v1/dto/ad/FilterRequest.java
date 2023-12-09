package com.api.admarket.api.v1.dto.ad;

import lombok.Data;

@Data
public class FilterRequest {

    private int page = 0;

    private int limit = 10;

    private String title = "";

}
