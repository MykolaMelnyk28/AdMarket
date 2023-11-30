package com.api.admarket.api.v1.repository;

import com.api.admarket.api.v1.entity.ad.AdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<AdEntity, Long> {
    @Query(value = """
        SELECT * FROM categories c WHERE c.name = :categoryName
        """, nativeQuery = true)
    List<AdEntity> findAllByCategoryName(String categoryName);

    @Query(value = """
            SELECT sa.ad FROM SavedAd sa WHERE sa.user.id = :userId
            """)
    List<AdEntity> findSavedAdsByUserId(Long userId);

    @Modifying
    @Query(value = """
            INSERT INTO users_ads(user_id, ad_id)
            VALUES (:userId, :adId)
            """, nativeQuery = true)
    void assignUser(Long userId, Long adId);

    @Modifying
    @Query(value = """
            INSERT INTO ad_images (ad_id, image_url)
            VALUES (:id, :url)
            """, nativeQuery = true)
    void addImage(Long id, String url);

}
