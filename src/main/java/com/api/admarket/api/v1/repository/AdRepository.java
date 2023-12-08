package com.api.admarket.api.v1.repository;

import com.api.admarket.api.v1.entity.ad.AdEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepository extends JpaRepository<AdEntity, Long> {

    @Query(value = """
            WITH RECURSIVE CategoryHierarchy AS (
                SELECT name, parent_name, is_leaf
                FROM categories
                WHERE name = :categoryName
                UNION ALL
                SELECT c.name, c.parent_name, c.is_leaf
                FROM categories c
                JOIN CategoryHierarchy ch ON c.parent_name = ch.name
            )
            SELECT a.* FROM ads a
            JOIN CategoryHierarchy ch ON a.category_name = ch.name
            WHERE a.title LIKE CONCAT('%', :title, '%')
            """, nativeQuery = true)
    Page<AdEntity> findAllByTitleContains(String categoryName, String title, Pageable pageable);

    Page<AdEntity> findAllByUserId(Long sellerId, Pageable pageable);

    @Query(value = """
            SELECT sa.ad FROM SavedAd sa WHERE sa.user.id = :userId
            """)
    Page<AdEntity> findSavedAdsByUserId(Long userId, Pageable pageable);

    @Modifying
    @Query(value = """
            INSERT INTO users_ads(user_id, ad_id)
            VALUES (:userId, :adId)
            """, nativeQuery = true)
    void assignUser(Long userId, Long adId);

    @Modifying
    @Query(value = """
            INSERT INTO ad_images (ad_id, image_url)
            VALUES (:adId, :url)
            """, nativeQuery = true)
    void addImage(Long adId, String url);

    @Modifying
    @Query(value = """
        DELETE FROM ad_images ui
        WHERE ui.ad_id = :adId AND ui.image_url = :url
        """, nativeQuery = true)
    void deleteImage(Long adId, String url);

}
