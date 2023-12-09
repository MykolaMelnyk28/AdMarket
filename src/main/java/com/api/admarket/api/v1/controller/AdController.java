package com.api.admarket.api.v1.controller;

import com.api.admarket.api.v1.dto.ad.AdDTO;
import com.api.admarket.api.v1.dto.mapper.AdMapper;
import com.api.admarket.api.v1.dto.validation.OnCreate;
import com.api.admarket.api.v1.dto.validation.OnUpdate;
import com.api.admarket.api.v1.entity.ad.AdEntity;
import com.api.admarket.api.v1.entity.user.UserEntity;
import com.api.admarket.api.v1.service.AdService;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/ads")
@AllArgsConstructor
@Validated
public class AdController {

    private final AdService adService;
    private final AdMapper adMapper;

    @GetMapping
    public ResponseEntity<Page<AdDTO>> getAll() {
        Page<AdEntity> pageEntity = adService.getAll(PageRequest.ofSize(10));
        Page<AdDTO> pageDto = pageEntity.map(adMapper::toDto);
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping("/{adId}")
    public ResponseEntity<AdDTO> getById(
            @PathVariable Long adId
    ) {
        Optional<AdEntity> found = adService.getById(adId);
        Optional<AdDTO> response = found.map(adMapper::toDto);
        adService.incrementViews(adId);
        return ResponseEntity.of(response);
    }

    @PutMapping("/{adId}")
    public ResponseEntity<AdDTO> updateById(
            @PathVariable Long adId,
            @RequestBody @Validated(OnUpdate.class) AdDTO request
    ) {
        AdEntity requestEntity = adMapper.toEntity(request);
        AdEntity updated = adService.updateById(adId, requestEntity);
        AdDTO response = adMapper.toDto(updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{adId}")
    public HttpStatus deleteById(
            @PathVariable Long adId
    ) {
        adService.deleteById(adId);
        return HttpStatus.OK;
    }
}
