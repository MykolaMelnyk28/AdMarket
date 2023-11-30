package com.api.admarket.api.v1.repository;

import com.api.admarket.api.v1.entity.message.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query(value = """
        SELECT * FROM chat_messages cm
        WHERE (cm.sender_id IN (:id1, :id2)) AND
              (cm.receiver_id IN (:id1, :id2));
        """, nativeQuery = true)
    List<ChatMessage> findAllMessagesBetween(Long id1, Long id2);

}
