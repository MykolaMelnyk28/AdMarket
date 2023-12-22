package com.api.admarket.api.v1.repository;

import com.api.admarket.api.v1.entity.user.AccountStatus;
import com.api.admarket.api.v1.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByPhoneNumber(String phoneNumber);
    Page<UserEntity> findAllByAccountStatus(AccountStatus accountStatus, Pageable pageable);

    @Query(value = """
        SELECT EXISTS(SELECT 1 FROM AdEntity a
        WHERE a.id = :adId AND a.user.id = :userId)
        """)
    boolean isAdSeller(@Param("userId") Long userId, @Param("adId") Long adId);

    @Modifying
    @Query(value = """
            INSERT INTO user_images (user_id, image_url)
            VALUES (:id, :url)
            """, nativeQuery = true)
    void addImage(Long id, String url);

    @Modifying
    @Query(value = """
        DELETE FROM user_images ui
        WHERE ui.user_id = :id AND ui.image_url = :url
        """, nativeQuery = true)
    void deleteImage(Long id, String url);
}
