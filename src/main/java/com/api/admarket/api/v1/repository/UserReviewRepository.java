package com.api.admarket.api.v1.repository;

import com.api.admarket.api.v1.entity.user.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, Long> {

}
