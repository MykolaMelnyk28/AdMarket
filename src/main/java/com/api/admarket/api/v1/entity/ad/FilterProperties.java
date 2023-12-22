package com.api.admarket.api.v1.entity.ad;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class FilterProperties {


    private int page;
    private int limit;
    private String title;
    private String category;
    private Sort sort;

}
