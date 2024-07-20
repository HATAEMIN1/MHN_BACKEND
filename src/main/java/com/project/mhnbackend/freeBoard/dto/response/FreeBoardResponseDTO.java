package com.project.mhnbackend.freeBoard.dto.response;

import java.time.LocalDateTime;

import com.project.mhnbackend.freeBoard.domain.FreeBoard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class FreeBoardResponseDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String memberName;

    public FreeBoardResponseDTO(Long id, String title, String content, LocalDateTime createdAt, String memberName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.memberName = memberName;
    }

 
}