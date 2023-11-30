package com.api.admarket.api.v1.repository;

import com.api.admarket.api.v1.entity.user.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, Long> {

    List<UserReview> findAllByReviewerId(Long reviewId);
    List<UserReview> findAllBySellerId(Long sellerId);
    List<UserReview> findAllByRatingBetween(int start, int end);

}
