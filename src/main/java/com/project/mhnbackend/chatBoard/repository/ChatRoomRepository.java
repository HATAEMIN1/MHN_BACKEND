package com.project.mhnbackend.chatBoard.repository;

import com.project.mhnbackend.chatBoard.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
    Optional<ChatRoom> findByChatRoomId(String chatRoomId);
}
