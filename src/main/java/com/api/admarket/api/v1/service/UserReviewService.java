package com.api.admarket.api.v1.service;

import com.api.admarket.api.v1.entity.user.UserReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserReviewService {

    UserReview create(UserReview review, Long reviewerId, Long sellerId);
    Optional<UserReview> getById(Long id);
    Optional<UserReview> getByContent(Long reviewerId, Long sellerId, String comment);
    Page<UserReview> getAllByReviewerId(Long reviewerId, Pageable pageable);
    Page<UserReview> getAllBySellerId(Long sellerId, Pageable pageable);
    Page<UserReview> getAll(Long reviewerId, Long sellerId, Pageable pageable);
    UserReview updateById(Long id, UserReview review);
    void deleteById(Long id);

}
