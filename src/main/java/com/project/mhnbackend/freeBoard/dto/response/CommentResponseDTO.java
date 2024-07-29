package com.project.mhnbackend.freeBoard.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponseDTO {
    private Long id;
    private String content;
    private Long memberId;
    private Long freeBoardId;
    private LocalDateTime createDate;
    private String nickName;
    private String profileImage; 
    private int step;
    private int depth;
    private int level;
    private List<CommentResponseDTO> replies;
}
