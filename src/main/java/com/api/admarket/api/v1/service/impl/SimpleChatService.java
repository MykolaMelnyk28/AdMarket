package com.api.admarket.api.v1.service.impl;

import com.api.admarket.api.v1.entity.message.Chat;
import com.api.admarket.api.v1.exeption.ResourceNotFoundException;
import com.api.admarket.api.v1.repository.ChatRepository;
import com.api.admarket.api.v1.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleChatService implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public Chat create(Chat chat, Long userId1, Long userId2) {
        if (chat.getId() != null && getById(chat.getId()).isPresent()) {
            throw new IllegalStateException("Chat already exists.");
        }
        Chat saved = chatRepository.save(chat);
        chatRepository.assignUsers(saved.getId(), userId1, userId2);
        return getOrThrow(saved.getId());
    }

    @Override
    public Optional<Chat> getById(Long chatId) {
        return chatRepository.findById(chatId);
    }

    @Override
    public Optional<Chat> getByUserIds(Long userId1, Long userId2) {
        return chatRepository.findChatByUserIds(userId1, userId2);
    }

    @Override
    public Optional<Chat> getByUserUsernames(String username1, String username2) {
        return chatRepository.findChatByUserUsernames(username1, username2);
    }

    @Override
    public Page<Chat> getAll(Pageable pageable) {
        return chatRepository.findAll(pageable);
    }

    @Override
    public Page<Chat> getAllByUserId(Long userId, Pageable pageable) {
        return chatRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Page<Chat> getAllByUserUsername(String username, Pageable pageable) {
        return chatRepository.findAllByUsername(username, pageable);
    }

    @Override
    public void deleteById(Long id) {
        chatRepository.deleteById(id);
    }

    private Chat getOrThrow(Long id) {
        return getById(id).orElseThrow(() ->
                new ResourceNotFoundException("Chat not found")
        );
    }
}
