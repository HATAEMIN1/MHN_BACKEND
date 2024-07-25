package com.project.mhnbackend.freeBoard.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.project.mhnbackend.freeBoard.domain.BoardImage;
import com.project.mhnbackend.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreeBoardResponseWithCommentsDTO {
    private Long id;
    private String title;
    private String content;
    private List<BoardImage> imageList;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private boolean likedByCurrentUser;
    private int likeCount;
    private int commentCount;
    private List<BoardCommentResponseDTO> comments;
    private Member member;
}
