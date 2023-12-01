package com.api.admarket.api.v1.service.impl;

import com.api.admarket.api.v1.entity.user.UserReview;
import com.api.admarket.api.v1.exeption.ResourceNotFoundException;
import com.api.admarket.api.v1.repository.UserReviewRepository;
import com.api.admarket.api.v1.service.UserReviewService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleUserReviewService implements UserReviewService {
    private final UserReviewRepository reviewRepository;

    @Override
    public UserReview create(UserReview review, Long reviewerId, Long sellerId) {
        if (getByContent(reviewerId, sellerId, review.getComment()).isPresent()) {
            throw new IllegalStateException("Review already exists.");
        }
        UserReview saved = reviewRepository.save(review);
        reviewRepository.assign(saved.getId(), reviewerId, sellerId);
        return saved;
    }

    @Override
    public Optional<UserReview> getById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public Optional<UserReview> getByContent(Long reviewerId, Long sellerId, String comment) {
        return reviewRepository.findByContent(reviewerId, sellerId, comment);
    }

    @Override
    public Page<UserReview> getAllByReviewerId(Long reviewerId, Pageable pageable) {
        return reviewRepository.findAllByReviewerId(reviewerId, pageable);
    }

    @Override
    public Page<UserReview> getAllBySellerId(Long sellerId, Pageable pageable) {
        return reviewRepository.findAllBySellerId(sellerId, pageable);
    }

    @Override
    public Page<UserReview> getAll(Long reviewerId, Long sellerId, Pageable pageable) {
        return reviewRepository.findAllBetween(reviewerId, sellerId, pageable);
    }

    @Override
    public UserReview updateById(Long id, UserReview review) {
        UserReview userReview = getOrThrow(id);
        userReview.setComment(review.getComment());
        userReview.setRating(review.getRating());
        UserReview updated = reviewRepository.save(userReview);
        return updated;
    }

    @Override
    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }

    private UserReview getOrThrow(Long id) throws ResourceNotFoundException {
        return reviewRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Review not found"));
    }
}
