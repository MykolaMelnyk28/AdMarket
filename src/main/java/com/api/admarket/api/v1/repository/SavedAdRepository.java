package com.api.admarket.api.v1.repository;

import com.api.admarket.api.v1.entity.ad.SavedAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedAdRepository extends JpaRepository<SavedAd, Long> {



}
