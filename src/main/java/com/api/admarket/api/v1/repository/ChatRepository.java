package com.api.admarket.api.v1.repository;

import com.api.admarket.api.v1.entity.message.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query(value = """
            SELECT c.* FROM chats_users cu
            JOIN chats c ON cu.chat_id = c.id
            JOIN users u ON cu.user_id = u.id
            WHERE u.id IN (:userId1, :userId2)
            """, nativeQuery = true)
    Optional<Chat> findChatByUserIds(Long userId1, Long userId2);

    @Query(value = """
            SELECT c.* FROM chats_users cu
            JOIN chats c ON cu.chat_id = c.id
            JOIN users u ON cu.user_id = u.id
            WHERE u.username IN (:username1, :username2)
            """, nativeQuery = true)
    Optional<Chat> findChatByUserUsernames(String username1, String username2);

    @Query(value = """
        SELECT c.* FROM chats_users cu
        JOIN chats c ON cu.chat_id = c.id
        JOIN users u ON cu.user_id = u.id
        WHERE u.id = :userId
        """, nativeQuery = true)
    Page<Chat> findAllByUserId(Long userId, Pageable pageable);

    @Query(value = """
        SELECT c.* FROM chats_users cu
        JOIN chats c ON cu.chat_id = c.id
        JOIN users u ON cu.user_id = u.id
        WHERE u.username = :username
        """, nativeQuery = true)
    Page<Chat> findAllByUsername(String username, Pageable pageable);

    @Modifying
    @Query(value = """
        INSERT INTO chats_users(chat_id, user_id)
        VALUES (:chatId, :userId1), (:chatId, :userId2);
        """, nativeQuery = true)
    void assignUsers(Long chatId, Long userId1, Long userId2);
}
