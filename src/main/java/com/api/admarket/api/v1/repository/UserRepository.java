package com.api.admarket.api.v1.repository;

import com.api.admarket.api.v1.entity.user.AccountStatus;
import com.api.admarket.api.v1.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByPhoneNumber(String phoneNumber);
    List<UserEntity> findAllByAccountStatus(AccountStatus accountStatus);

    @Modifying
    @Query(value = """
            INSERT INTO user_images (user_id, image_url)
            VALUES (:id, :url)
            """, nativeQuery = true)
    void addImage(Long id, String url);
}
