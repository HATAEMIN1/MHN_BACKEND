package com.project.mhnbackend.chatBoard.repository;

import com.project.mhnbackend.chatBoard.model.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    Optional<ChatRoomEntity> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
}
