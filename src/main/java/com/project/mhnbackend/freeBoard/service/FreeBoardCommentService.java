package com.project.mhnbackend.freeBoard.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
public class FreeBoardCommentService {

    @Autowired
    private FreeBoardCommentRepository freeBoardCommentRepository;

    @Autowired
    private FreeBoardRepository freeBoardRepository;

    @Autowired
    private MemberRepository memberRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

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
//    public CommentResponseDTO addComment(CommentRequestDTO commentRequestDTO) {
//        System.out.println("Received CommentRequestDTO: " + commentRequestDTO);
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
//    public CommentResponseDTO addComment(CommentRequestDTO commentRequestDTO) {
//        System.out.println("Received CommentRequestDTO: " + commentRequestDTO);
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
//                .replies(new ArrayList<>()) // 빈 리스트로 초기화
//                .build();
//
//        freeBoardCommentRepository.save(comment);
//
//        System.out.println("Saved Comment: " + comment);
//
//        return toCommentResponseDTO(comment);
//    }
    public FreeBoardComment addComment(CommentRequestDTO commentRequestDTO) {
        FreeBoard freeBoard = freeBoardRepository.findById(commentRequestDTO.getFreeBoardId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid FreeBoard ID"));
        Member member = memberRepository.findById(commentRequestDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Member ID"));

        FreeBoardComment parentComment = null;
        if (commentRequestDTO.getParentId() != null) {
            parentComment = freeBoardCommentRepository.findById(commentRequestDTO.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Parent Comment ID"));
        }

        FreeBoardComment comment = new FreeBoardComment();
        comment.setContent(commentRequestDTO.getContent());
        comment.setMember(member);
        comment.setFreeBoard(freeBoard);
        comment.setCreateDate(LocalDateTime.now());

        if (parentComment != null) {
            comment.setParent(parentComment);
            comment.setDepth(parentComment.getDepth() + 1);
            comment.setLevel(parentComment.getLevel() + 1);
            comment.setStep(parentComment.getStep() + 1);
        } else {
            comment.setDepth(0);
            comment.setLevel(0);
            comment.setStep(0);
        }

        return freeBoardCommentRepository.save(comment);
    }
//    public List<CommentResponseDTO> getCommentsByFreeBoardId(Long freeBoardId) {
//        List<FreeBoardComment> comments = freeBoardCommentRepository.findByFreeBoardIdAndParentIsNull(freeBoardId);
//        return comments.stream().map(this::toCommentResponseDTO).collect(Collectors.toList());
//    }
    public List<CommentResponseDTO> getCommentsByFreeBoardId(Long freeBoardId) {
        List<FreeBoardComment> comments = freeBoardCommentRepository.findByFreeBoardIdAndParentIsNull(freeBoardId);
        return comments.stream().map(this::toCommentResponseDTO).collect(Collectors.toList());
    }

    private CommentResponseDTO toCommentResponseDTO(FreeBoardComment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .memberId(comment.getMember().getId())
                .freeBoardId(comment.getFreeBoard().getId())
                .createDate(comment.getCreateDate())
                .nickName(comment.getMember().getNickName()) // 추가
                .profileImage(comment.getMember().getProfileImageUrl()) 
                .step(comment.getStep())
                .depth(comment.getDepth())
                .level(comment.getLevel())
                .replies(comment.getReplies().stream()
                        .map(this::toCommentResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public void editComment(Long commentId, String newContent) {
        FreeBoardComment comment = freeBoardCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Comment ID"));
        comment.setContent(newContent);
        freeBoardCommentRepository.save(comment);
    }

//    public void deleteComment(Long commentId) {
//        FreeBoardComment comment = freeBoardCommentRepository.findById(commentId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid Comment ID"));
//        freeBoardCommentRepository.delete(comment);
//    }
//    public void deleteCommentAndReplies(Long commentId) {
//        log.info("deleteCommentAndReplies called with commentId: {}", commentId);
//        FreeBoardComment comment = freeBoardCommentRepository.findById(commentId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid Comment ID: " + commentId));
//        deleteCommentRecursively(comment);
//        entityManager.flush(); // 데이터베이스에 즉시 반영
//    }
//
//    private void deleteCommentRecursively(FreeBoardComment comment) {
//        log.info("Deleting comment: {}", comment.getId());
//        List<FreeBoardComment> replies = freeBoardCommentRepository.findByParentId(comment.getId());
//        for (FreeBoardComment reply : replies) {
//            deleteCommentRecursively(reply);
//        }
//        try {
//            log.info("Attempting to delete comment: {}", comment.getId());
//            freeBoardCommentRepository.delete(comment);
//            log.info("Successfully deleted comment: {}", comment.getId());
//        } catch (Exception e) {
//            log.error("Could not delete comment: {} - {}", comment.getId(), e.getMessage(), e);
//            throw e;
//        }
//    }
//    public void deleteComment(Long commentId) {
//        Optional<FreeBoardComment> optionalComment = freeBoardCommentRepository.findById(commentId);
//        if (optionalComment.isPresent()) {
//            FreeBoardComment comment = optionalComment.get();
//            deleteReplies(comment);
//            freeBoardCommentRepository.delete(comment);
//            System.out.println("Deleted parent comment with id: " + commentId);
//        } else {
//            System.out.println("No comment found with id: " + commentId);
//        }
//    }
//
//    private void deleteReplies(FreeBoardComment comment) {
//        if (comment.getReplies() != null) {
//            for (FreeBoardComment reply : comment.getReplies()) {
//                deleteReplies(reply);
//                freeBoardCommentRepository.delete(reply);
//            }
//        }
//    }
//    @Transactional
//    public void deleteComment(Long commentId) {
//        Optional<FreeBoardComment> commentOptional = freeBoardCommentRepository.findById(commentId);
//        if (commentOptional.isPresent()) {
//            FreeBoardComment comment = commentOptional.get();
//            deleteReplies(comment);
//            freeBoardCommentRepository.delete(comment);
//        }
//    }
//
//    private void deleteReplies(FreeBoardComment parentComment) {
//        List<FreeBoardComment> replies = freeBoardCommentRepository.findByParentId(parentComment.getId());
//        for (FreeBoardComment reply : replies) {
//            deleteReplies(reply);
//            freeBoardCommentRepository.delete(reply);
//        }
//    
//    }
    @Transactional
    public void deleteComment(Long commentId) {
        freeBoardCommentRepository.deleteCommentAndReplies(commentId);
    }
//    private CommentResponseDTO toCommentResponseDTO(FreeBoardComment comment) {
//        return CommentResponseDTO.builder()
//                .id(comment.getId())
//                .content(comment.getContent())
//                .createDate(comment.getCreateDate())
//                .depth(comment.getDepth())
//                .level(comment.getLevel())
//                .step(comment.getStep())
//                .replies(comment.getReplies() != null ?
//                        comment.getReplies().stream()
//                                .map(this::toCommentResponseDTO)
//                                .collect(Collectors.toList()) : null)
//                .build();
//    }
}
