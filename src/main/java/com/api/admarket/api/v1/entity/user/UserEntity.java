package com.api.admarket.api.v1.entity.user;

import com.api.admarket.api.v1.entity.ad.AdEntity;
import com.api.admarket.api.v1.entity.ad.SavedAd;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.NOACTIVATE;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @CreationTimestamp
    private LocalDateTime dateRegistered;

    @UpdateTimestamp
    private LocalDateTime dateUpdated;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_info_id", referencedColumnName = "id")
    private UserInfo userInfo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_images", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "image_url", nullable = false)
    private List<String> imageUrls;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<SavedAd> savedAds;
}
