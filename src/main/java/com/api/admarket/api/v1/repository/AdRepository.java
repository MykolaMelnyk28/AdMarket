package com.api.admarket.api.v1.repository;

import com.api.admarket.api.v1.entity.ad.AdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepository extends JpaRepository<AdEntity, Long> {

}
