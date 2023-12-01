package com.api.admarket.api.v1.service;

import com.api.admarket.api.v1.entity.message.Chat;
import com.api.admarket.api.v1.entity.message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ChatService {

    Chat create(Chat chat, Long userId1, Long userId2);
    Optional<Chat> getById(Long chatId);
    Optional<Chat> getByUserIds(Long userId1, Long userId2);
    Optional<Chat> getByUserUsernames(String username1, String username2);
    Page<Chat> getAll(Pageable pageable);
    Page<Chat> getAllByUserId(Long userId, Pageable pageable);
    Page<Chat> getAllByUserUsername(String username, Pageable pageable);
    void deleteById(Long chatId);

}
