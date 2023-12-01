package com.api.admarket.api.v1.service.impl;

import com.api.admarket.api.v1.entity.message.Message;
import com.api.admarket.api.v1.exeption.ResourceNotFoundException;
import com.api.admarket.api.v1.repository.MessageRepository;
import com.api.admarket.api.v1.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleMessageService implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public Message create(Message message, Long chatId, Long receiverId, Long senderId) {
        if (message.getId() != null && getById(message.getId()).isPresent()) {
            throw new IllegalStateException("Message already exists.");
        }
        Message saved = messageRepository.save(message);
        messageRepository.assignChat(saved.getId(), chatId);
        messageRepository.assignReceiver(saved.getId(), receiverId);
        messageRepository.assignSender(saved.getId(), senderId);
        return getOrThrow(saved.getId());
    }

    @Override
    public Optional<Message> getById(Long messageId) {
        return messageRepository.findById(messageId);
    }

    @Override
    public Page<Message> getAll(Long chatId, Pageable pageable) {
        return messageRepository.findAllByChatId(chatId, pageable);
    }

    @Override
    public Message updateById(Long messageId, Message message) {
        Message found = getOrThrow(messageId);
        found.setText(message.getText());
        found.setRead(message.isRead());
        return messageRepository.save(found);
    }

    @Override
    public void deleteById(Long messageId) {
        messageRepository.deleteById(messageId);
    }

    @Override
    public void deleteAllByChatId(Long chatId) {
        messageRepository.deleteAllByChatId(chatId);
    }

    private Message getOrThrow(Long messageId) {
        return getById(messageId).orElseThrow(() ->
                new ResourceNotFoundException("Message not found")
        );
    }
}
