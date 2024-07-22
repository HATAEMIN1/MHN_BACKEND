package com.project.mhnbackend.freeBoard.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRequestDTO {
    private Long freeBoardId;
    private Long memberId;
    private String content;
    private Long parentId; // 대댓글의 경우 부모 댓글 ID
}
