package com.api.admarket.api.v1.repository;

import com.api.admarket.api.v1.entity.ad.FilterProperties;
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
            SELECT id, name, parent_id
            FROM categories
            WHERE name = :#{#filterPs.category}
            UNION ALL
            SELECT c.id, c.name, c.parent_id
            FROM categories c
            JOIN CategoryHierarchy ch ON c.parent_id = ch.id
        )
        SELECT a.*, ua.user_id FROM ads a
        JOIN users_ads ua ON ua.ad_id = a.id
        JOIN CategoryHierarchy ch ON a.category_id = ch.id
        WHERE a.title LIKE CONCAT('%', :#{#filterPs.title}, '%')
        """, countQuery = """
        WITH RECURSIVE CategoryHierarchy AS (
            SELECT id, name, parent_id
            FROM categories
            WHERE name = :#{#filterPs.category}
            UNION ALL
            SELECT c.id, c.name, c.parent_id
            FROM categories c
            JOIN CategoryHierarchy ch ON c.parent_id = ch.id
        )
        SELECT COUNT(DISTINCT a.id) FROM ads a
        JOIN users_ads ua ON ua.ad_id = a.id
        JOIN CategoryHierarchy ch ON a.category_id = ch.id
        WHERE a.title LIKE CONCAT('%', :#{#filterPs.title}, '%')
        """, nativeQuery = true)
    Page<AdEntity> findAdsByFilters(FilterProperties filterPs, Pageable pageable);

    Page<AdEntity> findAllByUserId(Long sellerId, Pageable pageable);

    @Modifying
    @Query(value = """
        DELETE FROM AdEntity a WHERE a.id = :id
        """)
    void deleteById(Long id);

    @Modifying
    @Query(value = """
        INSERT INTO saved_ads(user_id, ad_id)
        VALUES (:userId, :adId);
        """, nativeQuery = true)
    void addSaveAd(Long userId, Long adId);

    @Query(value = """
            SELECT sa.ad FROM SavedAd sa WHERE sa.user.id = :userId
            """)
    Page<AdEntity> findSavedAdsByUserId(Long userId, Pageable pageable);

    @Modifying
    @Query("""
        DELETE FROM SavedAd sa
        WHERE sa.user.id = :userId AND sa.ad.id = :adId
        """)
    void deleteSavedAdById(Long userId, Long adId);

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

    @Modifying
    @Query(value = """
        UPDATE ads a 
        SET a.views_count = a.views_count + 1
        WHERE a.id = :id
        """, nativeQuery = true)
    void incrementViewsCountById(Long id);


}
