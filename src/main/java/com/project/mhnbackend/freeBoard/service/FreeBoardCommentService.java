package com.project.mhnbackend.freeBoard.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.mhnbackend.freeBoard.domain.FreeBoard;
import com.project.mhnbackend.freeBoard.domain.FreeBoardComment;
import com.project.mhnbackend.freeBoard.dto.request.CommentRequestDTO;
import com.project.mhnbackend.freeBoard.dto.response.CommentResponseDTO;
import com.project.mhnbackend.freeBoard.repository.FreeBoardCommentRepository;
import com.project.mhnbackend.freeBoard.repository.FreeBoardRepository;
import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.member.repository.MemberRepository;

@Service
@Transactional
public class FreeBoardCommentService {

    @Autowired
    private FreeBoardCommentRepository freeBoardCommentRepository;

    @Autowired
    private FreeBoardRepository freeBoardRepository;

    @Autowired
    private MemberRepository memberRepository;

//    public CommentResponseDTO addComment(CommentRequestDTO commentRequestDTO) {
//    	 System.out.println("Received CommentRequestDTO: " + commentRequestDTO);
//        FreeBoard freeBoard = freeBoardRepository.findById(commentRequestDTO.getFreeBoardId())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid FreeBoard ID"));
//
//        Member member = memberRepository.findById(commentRequestDTO.getMemberId())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid Member ID"));
//
//        FreeBoardComment parentComment = null;
//        if (commentRequestDTO.getParentId() != null) {
//            parentComment = freeBoardCommentRepository.findById(commentRequestDTO.getParentId())
//                    .orElseThrow(() -> new IllegalArgumentException("Invalid Parent Comment ID"));
//        }
//
//        FreeBoardComment comment = FreeBoardComment.builder()
//                .content(commentRequestDTO.getContent())
//                .member(member)
//                .freeBoard(freeBoard)
//                .parent(parentComment)
//                .step(parentComment != null ? parentComment.getStep() + 1 : 0)
//                .depth(parentComment != null ? parentComment.getDepth() + 1 : 0)
//                .level(parentComment != null ? parentComment.getLevel() + 1 : 0)
//                .build();
//
//        freeBoardCommentRepository.save(comment);
//        
//        System.out.println("Saved Comment: " + comment);
//
//        return toCommentResponseDTO(comment);
//    }
//
//    public List<CommentResponseDTO> getCommentsByFreeBoardId(Long freeBoardId) {
//        List<FreeBoardComment> comments = freeBoardCommentRepository.findByFreeBoardIdAndParentIsNull(freeBoardId);
//        return comments.stream().map(this::toCommentResponseDTO).collect(Collectors.toList());
//    }
    public CommentResponseDTO addComment(CommentRequestDTO commentRequestDTO) {
        System.out.println("Received CommentRequestDTO: " + commentRequestDTO);
        FreeBoard freeBoard = freeBoardRepository.findById(commentRequestDTO.getFreeBoardId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid FreeBoard ID"));

        Member member = memberRepository.findById(commentRequestDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Member ID"));

        FreeBoardComment parentComment = null;
        if (commentRequestDTO.getParentId() != null) {
            parentComment = freeBoardCommentRepository.findById(commentRequestDTO.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Parent Comment ID"));
        }

        FreeBoardComment comment = FreeBoardComment.builder()
                .content(commentRequestDTO.getContent())
                .member(member)
                .freeBoard(freeBoard)
                .parent(parentComment)
                .step(parentComment != null ? parentComment.getStep() + 1 : 0)
                .depth(parentComment != null ? parentComment.getDepth() + 1 : 0)
                .level(parentComment != null ? parentComment.getLevel() + 1 : 0)
                .build();

        freeBoardCommentRepository.save(comment);
        
        System.out.println("Saved Comment: " + comment);

        return toCommentResponseDTO(comment);
    }

    public List<CommentResponseDTO> getCommentsByFreeBoardId(Long freeBoardId) {
        List<FreeBoardComment> comments = freeBoardCommentRepository.findByFreeBoardIdAndParentIsNull(freeBoardId);
        return comments.stream().map(this::toCommentResponseDTO).collect(Collectors.toList());
    }

    public void editComment(Long commentId, String newContent) {
        FreeBoardComment comment = freeBoardCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Comment ID"));
        comment.setContent(newContent);
        freeBoardCommentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        FreeBoardComment comment = freeBoardCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Comment ID"));
        freeBoardCommentRepository.delete(comment);
    }

    private CommentResponseDTO toCommentResponseDTO(FreeBoardComment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createDate(comment.getCreateDate())
                .depth(comment.getDepth())
                .level(comment.getLevel())
                .step(comment.getStep())
                .replies(comment.getReplies() != null ?
                        comment.getReplies().stream()
                                .map(this::toCommentResponseDTO)
                                .collect(Collectors.toList()) : null)
                .build();
    }
}
