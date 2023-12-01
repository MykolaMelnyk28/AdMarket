package com.api.admarket.api.v1.repository;

import com.api.admarket.api.v1.entity.user.UserReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, Long> {

    @Query(value = """
        SELECT * FROM user_reviews ur
        WHERE ur.reviewer_id = :reviewerId AND ur.seller_id = :sellerId AND ur.comment = :comment
        LIMIT 1
        """, nativeQuery = true)
    Optional<UserReview> findByContent(Long reviewerId, Long sellerId, String comment);
    Page<UserReview> findAllByReviewerId(Long reviewerId, Pageable pageable);
    Page<UserReview> findAllBySellerId(Long sellerId, Pageable pageable);

    @Query(value = """
        SELECT * FROM user_reviews ur
        WHERE ur.reviewer_id = :reviewerId AND ur.seller_id = :sellerId
        """, nativeQuery = true)
    Page<UserReview> findAllBetween(Long reviewerId, Long sellerId, Pageable pageable);

    Page<UserReview> findAllByRatingBetween(int start, int end, Pageable pageable);

    @Modifying
    @Query(value = """
        UPDATE user_reviews ur
        SET ur.reviewer_id = :reviewerId, ur.seller_id = :sellerId
        WHERE ur.id = :id
        """, nativeQuery = true)
    void assign(Long id, Long reviewerId, Long sellerId);

}
