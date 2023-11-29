package com.api.admarket.api.v1.entity.user;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_reviews", uniqueConstraints = {
        @UniqueConstraint(
                name = "idx_review",
                columnNames = {"reviewer_id", "seller_id", "comment"}
        )
})
public class UserReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Check(constraints = "rating >= 1 AND rating <= 5")
    private int rating;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reviewer_id")
    private UserEntity reviewer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id")
    private UserEntity seller;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String comment;

    @CreationTimestamp
    private LocalDateTime dateCreated;

    @UpdateTimestamp
    private LocalDateTime dateUpdated;

}
