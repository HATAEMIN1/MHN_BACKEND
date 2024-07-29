package com.project.mhnbackend.freeBoard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.project.mhnbackend.freeBoard.domain.FreeBoardComment;
import com.project.mhnbackend.freeBoard.dto.request.CommentRequestDTO;
import com.project.mhnbackend.freeBoard.dto.response.CommentResponseDTO;
import com.project.mhnbackend.freeBoard.service.FreeBoardCommentService;
import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.member.repository.MemberRepository;

import lombok.extern.log4j.Log4j2;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Log4j2
public class FreeBoardCommentController {

	@Autowired
	private FreeBoardCommentService freeBoardCommentService;

	@Autowired
	private MemberRepository memberRepository;

//    @PostMapping("/boards/comment")
//    public ResponseEntity<CommentResponseDTO> addComment(
//            @RequestParam("freeBoardId") Long freeBoardId,
//            @RequestParam("content") String content,
//            @RequestParam(value = "parentId", required = false) Long parentId,
//            Authentication authentication) {
//        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
//        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid member email"));
//        
//        CommentRequestDTO commentRequestDTO = CommentRequestDTO.builder()
//                .freeBoardId(freeBoardId)
//                .memberId(member.getId())
//                .content(content)
//                .parentId(parentId)
//                .build();
//        System.out.println("Received CommentRequestDTO in Controller: " + commentRequestDTO);
//        CommentResponseDTO commentResponseDTO = freeBoardCommentService.addComment(commentRequestDTO);
//        return new ResponseEntity<>(commentResponseDTO, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/boards/comment")
//    public ResponseEntity<List<CommentResponseDTO>> getCommentsByFreeBoardId(@RequestParam("freeBoardId") Long freeBoardId) {
//        List<CommentResponseDTO> comments = freeBoardCommentService.getCommentsByFreeBoardId(freeBoardId);
//        return new ResponseEntity<>(comments, HttpStatus.OK);
//    }
//    @PostMapping("/boards/comment")
//    public ResponseEntity<CommentResponseDTO> addComment(
//            @RequestParam("freeBoardId") Long freeBoardId,
//            @RequestParam("memberId") Long memberId,
//            @RequestParam("content") String content,
//            @RequestParam(value = "parentId", required = false) Long parentId) {
//
//        CommentRequestDTO commentRequestDTO = CommentRequestDTO.builder()
//                .freeBoardId(freeBoardId)
//                .memberId(memberId)
//                .content(content)
//                .parentId(parentId)
//                .build();
//        System.out.println("Received CommentRequestDTO in Controller: " + commentRequestDTO);
//        CommentResponseDTO commentResponseDTO = freeBoardCommentService.addComment(commentRequestDTO);
//        return new ResponseEntity<>(commentResponseDTO, HttpStatus.CREATED);
//    }
//	@PostMapping("/boards/comment")
//	public ResponseEntity<CommentResponseDTO> addComment(@RequestParam("freeBoardId") Long freeBoardId,
//			@RequestParam("memberId") Long memberId, @RequestParam("content") String content,
//			@RequestParam(value = "parentId", required = false) Long parentId) {
//
//		CommentRequestDTO commentRequestDTO = CommentRequestDTO.builder().freeBoardId(freeBoardId).memberId(memberId)
//				.content(content).parentId(parentId).build();
//		System.out.println("Received CommentRequestDTO in Controller: " + commentRequestDTO);
//		CommentResponseDTO commentResponseDTO = freeBoardCommentService.addComment(commentRequestDTO);
//		return new ResponseEntity<>(commentResponseDTO, HttpStatus.CREATED);
//	}
	 @PostMapping("/boards/comment")
	 public ResponseEntity<FreeBoardComment> addComment(@RequestBody CommentRequestDTO commentRequestDTO) {
	        FreeBoardComment comment = freeBoardCommentService.addComment(commentRequestDTO);
	        return ResponseEntity.ok(comment);
	    }


//    @GetMapping("/boards/comment")
//    public ResponseEntity<List<CommentResponseDTO>> getCommentsByFreeBoardId(@RequestParam("freeBoardId") Long freeBoardId) {
//        List<CommentResponseDTO> comments = freeBoardCommentService.getCommentsByFreeBoardId(freeBoardId);
//        return new ResponseEntity<>(comments, HttpStatus.OK);
//    }
	@GetMapping("/boards/comment")
	public ResponseEntity<List<CommentResponseDTO>> getCommentsByFreeBoardId(
			@RequestParam("freeBoardId") Long freeBoardId) {
		List<CommentResponseDTO> comments = freeBoardCommentService.getCommentsByFreeBoardId(freeBoardId);
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}

	@PutMapping("/boards/editcomment")
	public ResponseEntity<Void> editComment(@RequestParam("commentId") Long commentId,
			@RequestParam("newContent") String newContent) {
		freeBoardCommentService.editComment(commentId, newContent);
		return new ResponseEntity<>(HttpStatus.OK);
	}

//    @DeleteMapping("/boards/delcomment")
//    public ResponseEntity<Void> deleteComment(@RequestParam(name = "commentId") Long commentId) {
//        try {
//            freeBoardCommentService.deleteCommentAndReplies(commentId);
//            return ResponseEntity.noContent().build();
//        } catch (IllegalArgumentException e) {
//            log.error("Invalid Comment ID: {}", commentId, e);
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        } catch (Exception e) {
//            log.error("Error deleting comment: {}", commentId, e);
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete comment");
//        }
//    }
//	@DeleteMapping("/boards/delcomment")
//	public ResponseEntity<Void> deleteComment(@RequestParam("commentId") Long id) {
//        freeBoardCommentService.deleteComment(id);
//        return ResponseEntity.noContent().build();
//    }
	@DeleteMapping("/boards/delcomment")
	public void deleteComment(@RequestParam("commentId") Long id) {
        freeBoardCommentService.deleteComment(id);
    }
}
