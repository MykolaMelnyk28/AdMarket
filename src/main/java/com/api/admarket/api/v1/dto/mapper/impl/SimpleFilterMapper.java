package com.api.admarket.api.v1.dto.mapper.impl;

import com.api.admarket.api.v1.dto.mapper.FiltersMapper;
import com.api.admarket.api.v1.entity.ad.FilterProperties;
import com.api.admarket.api.v1.dto.ad.FilterRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction;
import static org.springframework.data.domain.Sort.Order;

@Component
public class SimpleFilterMapper implements FiltersMapper {

    @Override
    public FilterRequest toDto(FilterProperties entity) {
        if (entity == null) {
            return null;
        }

        FilterRequest dto = new FilterRequest();
        dto.setPage(entity.getPage());
        dto.setLimit(entity.getLimit());
        dto.setTitle(entity.getTitle());
        dto.setCategory(entity.getCategory());
        dto.setSort(getSortPattern(entity.getSort()));

        return dto;
    }

    private String getSortPattern(Sort sort) {
        if (sort == null) {
            return "title asc";
        }
        List<Order> orders = sort.toList();
        List<String> fieldDirectionPairs = new ArrayList<>();

        orders.forEach(order -> {
            String property = order.getProperty();
            String direction = order.getDirection().toString().toLowerCase();
            String pair = String.join(" ", property, direction);
            fieldDirectionPairs.add(pair);
        });

        return String.join(",", fieldDirectionPairs);
    }

    @Override
    public FilterProperties toEntity(FilterRequest dto) {
        if (dto == null) {
            return null;
        }

        FilterProperties entity = new FilterProperties();

        entity.setPage(dto.getPage());
        entity.setLimit(dto.getLimit());
        entity.setTitle(dto.getTitle());
        entity.setSort(getSort(dto.getSort()));
        entity.setCategory(dto.getCategory());

        return entity;
    }

    private Sort getSort(String sortPattern) {
        if (sortPattern == null) {
            return Sort.unsorted();
        }

        String[] fieldDirectionPairs = sortPattern.trim().split(",");
        List<Order> orders = new ArrayList<>();

        for(String fieldDirectionPair : fieldDirectionPairs) {
            String[] fieldDirection = fieldDirectionPair.split(" ");
            String field = fieldDirection[0];

            String direction = fieldDirection[1];
            Direction dir = Direction.fromString(direction);

            if (dir.isAscending()) {
                orders.add(Order.asc(field));
            } else {
                orders.add(Order.desc(field));
            }
        }

        return Sort.by(orders);
    }

}
