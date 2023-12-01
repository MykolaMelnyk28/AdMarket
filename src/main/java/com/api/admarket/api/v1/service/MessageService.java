package com.api.admarket.api.v1.service;

import com.api.admarket.api.v1.entity.message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MessageService {

    Message create(Message message, Long chatId, Long receiverId, Long senderId);
    Optional<Message> getById(Long chatId);
    Page<Message> getAll(Long chatId, Pageable pageable);
    Message updateById(Long messageId, Message message);
    void deleteById(Long messageId);
    void deleteAllByChatId(Long chatId);

}
