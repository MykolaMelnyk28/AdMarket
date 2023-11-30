package com.api.admarket.api.v1.repository;

import com.api.admarket.api.v1.entity.ad.SavedAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedAdRepository extends JpaRepository<SavedAd, Long> {

    List<SavedAd> findAllByUserId(Long id);

}
