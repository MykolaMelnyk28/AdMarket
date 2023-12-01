package com.api.admarket.api.v1.repository;

import com.api.admarket.api.v1.entity.message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findAllByChatId(Long chatId, Pageable pageable);

    @Query(value = """
        SELECT COUNT(*) FROM messages m
        WHERE m.receiver_id = :userId AND m.is_read = :isRead
        """, nativeQuery = true)
    int countReadByReceiverId(Long userId, boolean isRead);

    @Query(value = """
        UPDATE messages m
        SET m.chat_id = :chatId
        WHERE m.id = :messageId
        """, nativeQuery = true)
    void assignChat(Long messageId, Long chatId);

    @Query(value = """
        UPDATE messages m
        SET m.sender_id = :userId
        WHERE m.id = :messageId
        """, nativeQuery = true)
    void assignSender(Long messageId, Long userId);

    @Query(value = """
        UPDATE messages m
        SET m.receiver_id = :userId
        WHERE m.id = :messageId
        """, nativeQuery = true)
    void assignReceiver(Long messageId, Long userId);

    void deleteAllByChatId(Long id);
}
