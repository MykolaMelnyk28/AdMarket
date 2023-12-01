package com.api.admarket.api.v1.entity.message;

import com.api.admarket.api.v1.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiver_id")
    private UserEntity receiver;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

    @Column
    private boolean isRead = false;

    @CreationTimestamp
    private LocalDateTime dateCreated;

    @UpdateTimestamp
    private LocalDateTime dateUpdated;

}