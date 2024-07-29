package com.project.mhnbackend.freeBoard.service;

import com.project.mhnbackend.common.util.FileUploadUtil;
import com.project.mhnbackend.freeBoard.domain.BoardImage;
import com.project.mhnbackend.freeBoard.domain.FreeBoard;
import com.project.mhnbackend.freeBoard.domain.FreeBoardLikes;
import com.project.mhnbackend.freeBoard.dto.request.EditFreeBoardDTO;
import com.project.mhnbackend.freeBoard.dto.request.FreeBoardDTO;
import com.project.mhnbackend.freeBoard.dto.request.FreeBoardRequestDTO;
import com.project.mhnbackend.freeBoard.dto.response.BoardCommentResponseDTO;
import com.project.mhnbackend.freeBoard.dto.response.FreeBoardResponseWithCommentsDTO;
import com.project.mhnbackend.freeBoard.dto.response.FreeBoardResponseDTO;
import com.project.mhnbackend.freeBoard.repository.FreeBoardLikesRepository;
import com.project.mhnbackend.freeBoard.repository.FreeBoardRepository;
import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.member.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FreeBoardService {

    @Autowired
    private final FreeBoardRepository freeBoardRepository;
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final FreeBoardLikesRepository freeBoardLikesRepository;
    @Autowired
    private final FileUploadUtil fileUploadUtil;

    public Long register(FreeBoardRequestDTO freeBoardRequestDTO) {
        FreeBoard freeBoard = dtoToEntity(freeBoardRequestDTO);
        Long id = freeBoardRepository.save(freeBoard).getId();
        return id;
    }

    private FreeBoard dtoToEntity(FreeBoardRequestDTO freeBoardRequestDTO) {
        FreeBoard freeBoard = FreeBoard.builder().title(freeBoardRequestDTO.getTitle())
                .content(freeBoardRequestDTO.getContent()).build();
        List<String> upLoadFileNames = freeBoardRequestDTO.getUploadFileNames();
        if (upLoadFileNames == null || upLoadFileNames.isEmpty()) {
            return freeBoard;
        }
        upLoadFileNames.forEach(fileName -> {
            freeBoard.addImageString(fileName);
        });
        return freeBoard;
    }

//    public Page<FreeBoardResponseDTO> pageFreeBoard(Pageable pageable) {
//        Page<FreeBoard> freeBoards = freeBoardRepository.pageFreeBoard(pageable);
//        Member currentUser = getCurrentUser();
//        return freeBoards.map(freeBoard -> {
//            boolean likeState = freeBoardLikesRepository.existsByFreeBoardAndMember(freeBoard, currentUser);
//            int likeCount = freeBoard.getLikes().size();
//            int commentCount = freeBoard.getComments().size(); 
//            return FreeBoardResponseDTO.builder()
//                    .id(freeBoard.getId())
//                    .title(freeBoard.getTitle())
//                    .content(freeBoard.getContent())
//                    .createDate(freeBoard.getCreateDate())
//                    .updateDate(freeBoard.getUpdateDate())
//                    .imageList(freeBoard.getImageList())
//                    .likeState(likeState)
//                    .likeCount(likeCount)
//                    .commentCount(commentCount)
//                    .build();
//        });
//    }
//
//    public FreeBoardResponseWithCommentsDTO getFreeBoard(Long freeBoardId, Long memberId) {
//        FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId).orElseThrow();
//        Member member = memberRepository.findById(memberId).orElseThrow();
//        boolean likeState = freeBoardLikesRepository.existsByFreeBoardAndMember(freeBoard, member);
//        int likeCount = freeBoard.getLikes().size();
//        int commentCount = freeBoard.getComments().size();
//
//        List<BoardCommentResponseDTO> comments = freeBoard.getComments().stream()
//                .map(comment -> BoardCommentResponseDTO.builder()
//                        .id(comment.getId())
//                        .content(comment.getContent())
//                        .memberName(comment.getMember().getName())
//                        .createDate(comment.getCreateDate())
//                        .depth(comment.getDepth())
//                        .build())
//                .collect(Collectors.toList());
//
//        return FreeBoardResponseWithCommentsDTO.builder()
//                .id(freeBoard.getId())
//                .title(freeBoard.getTitle())
//                .content(freeBoard.getContent())
//                .createDate(freeBoard.getCreateDate())
//                .updateDate(freeBoard.getUpdateDate())
//                .imageList(freeBoard.getImageList())
//                .likeState(likeState)
//                .likeCount(likeCount)
//                .commentCount(commentCount)
//                .comments(comments)
//                .build();
//    }
    public Page<FreeBoardResponseDTO> pageFreeBoard(Pageable pageable) {
        Page<FreeBoard> freeBoards = freeBoardRepository.pageFreeBoard(pageable);
        return freeBoards.map(freeBoard -> {
            int likeCount = freeBoard.getLikes().size();
            int commentCount = freeBoard.getComments().size();
            return FreeBoardResponseDTO.builder()
                    .id(freeBoard.getId())
                    .title(freeBoard.getTitle())
                    .content(freeBoard.getContent())
                    .createDate(freeBoard.getCreateDate())
                    .updateDate(freeBoard.getUpdateDate())
                    .imageList(freeBoard.getImageList())
                    .likeState(false) // 기본값으로 설정
                    .likeCount(likeCount)
                    .commentCount(commentCount)
                    .build();
        });
    }

//    public FreeBoardResponseWithCommentsDTO getFreeBoard(Long freeBoardId, Long memberId) {
//        FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid freeBoard ID"));
//
//        int likeCount = freeBoard.getLikes().size();
//        int commentCount = freeBoard.getComments().size();
//
//        boolean likedByCurrentUser = freeBoardLikesRepository.existsByFreeBoardAndMember(freeBoard, memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID")));
//
//        List<BoardCommentResponseDTO> comments = freeBoard.getComments().stream()
//                .map(comment -> BoardCommentResponseDTO.builder()
//                        .id(comment.getId())
//                        .content(comment.getContent())
//                        .memberName(comment.getMember().getName())
//                        .createDate(comment.getCreateDate())
//                        .depth(comment.getDepth())
//                        .build())
//                .collect(Collectors.toList());
//
//        return FreeBoardResponseWithCommentsDTO.builder()
//                .id(freeBoard.getId())
//                .title(freeBoard.getTitle())
//                .content(freeBoard.getContent())
//                .createDate(freeBoard.getCreateDate())
//                .updateDate(freeBoard.getUpdateDate())
//                .imageList(freeBoard.getImageList())
//                .likedByCurrentUser(likedByCurrentUser) // 필드 이름 수정
//                .likeCount(likeCount)
//                .commentCount(commentCount)
//                .comments(comments)
//                .build();
//    }

    public FreeBoardResponseWithCommentsDTO getFreeBoard(Long freeBoardId, Long memberId) {
        FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid freeBoard ID"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        int likeCount = freeBoard.getLikes().size();
        int commentCount = freeBoard.getComments().size();

        boolean likedByCurrentUser = freeBoardLikesRepository.existsByFreeBoardAndMember(freeBoard, member);

        List<BoardCommentResponseDTO> comments = freeBoard.getComments().stream()
                .map(comment -> BoardCommentResponseDTO.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .memberName(comment.getMember().getName())
                        .createDate(comment.getCreateDate())
                        .depth(comment.getDepth())
                        .build())
                .collect(Collectors.toList());

        return FreeBoardResponseWithCommentsDTO.builder()
                .id(freeBoard.getId())
                .title(freeBoard.getTitle())
                .content(freeBoard.getContent())
                .createDate(freeBoard.getCreateDate())
                .updateDate(freeBoard.getUpdateDate())
                .imageList(freeBoard.getImageList())
                .likedByCurrentUser(likedByCurrentUser)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .comments(comments)
                .member(freeBoard.getMember()) // 멤버 정보 설정
                .build();
    }

    private Member getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        Member member = memberRepository.getWithRole(email);
        return member;
    }

    public void editFreeBoard(Long freeBoardId, EditFreeBoardDTO editFreeBoardDTO) {
        FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId)
                .orElseThrow(() -> new RuntimeException("게시판 아이디가 없음"));
        freeBoard.changeTitleAndContent(editFreeBoardDTO.getTitle(), editFreeBoardDTO.getContent());
        freeBoardRepository.save(freeBoard);
    }

//    public void deleteFreeBoard(Long freeBoardId) {
//        FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId)
//                .orElseThrow(() -> new RuntimeException("게시판 아이디가 없음"));
//        freeBoardRepository.delete(freeBoard);
//    }
    public void deleteFreeBoard(Long freeBoardId, Long memberId) {
        FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId)
                .orElseThrow(() -> new RuntimeException("게시판 아이디가 없음"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 아이디가 없음"));

        // 작성자인지 확인
        if (!freeBoard.getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("작성자가 아닙니다.");
        }
        
        freeBoardRepository.delete(freeBoard);
    }
    

    public FreeBoard postFreeBoard(FreeBoardDTO freeBoardDTO, Member member) {
        FreeBoard freeBoard = FreeBoard.builder()
                .title(freeBoardDTO.getTitle())
                .content(freeBoardDTO.getContent())
                .member(member)
                .build();
        List<BoardImage> images = freeBoardDTO.getUploadFileNames().stream()
                .map(fileName -> BoardImage.builder().fileName(fileName).build())
                .collect(Collectors.toList());
        images.forEach(freeBoard::addImage);
        return freeBoardRepository.save(freeBoard);
    }

    public FreeBoard createFreeBoard(FreeBoardRequestDTO freeBoardDTO) {
        Member member = memberRepository.findById(freeBoardDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        FreeBoard freeBoard = FreeBoard.builder()
                .title(freeBoardDTO.getTitle())
                .content(freeBoardDTO.getContent())
                .member(member)
                .build();

        List<String> savedFileNames = fileUploadUtil.saveFiles(freeBoardDTO.getFiles());
        savedFileNames.forEach(freeBoard::addImageString);

        return freeBoardRepository.save(freeBoard);
    }

//    public void likeFreeBoard(Long freeBoardId, Long memberId) {
//        FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid freeBoard ID"));
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
//
//        if (freeBoardLikesRepository.existsByFreeBoardAndMember(freeBoard, member)) {
//            throw new IllegalStateException("Member already liked this post");
//        }
//
//        FreeBoardLikes like = FreeBoardLikes.builder()
//                .freeBoard(freeBoard)
//                .member(member)
//                .build();
//        freeBoardLikesRepository.save(like);
//    }
//
//    public void unlikeFreeBoard(Long freeBoardId, Long memberId) {
//        FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid freeBoard ID"));
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
//
//        FreeBoardLikes like = freeBoardLikesRepository.findByFreeBoardAndMember(freeBoard, member)
//                .orElseThrow(() -> new IllegalStateException("Like not found"));
//        freeBoardLikesRepository.delete(like);
//    }
    public void likeFreeBoard(Long freeBoardId, Long memberId) {
        FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid freeBoard ID"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        if (freeBoardLikesRepository.existsByFreeBoardAndMember(freeBoard, member)) {
            throw new IllegalStateException("Member already liked this post");
        }

        FreeBoardLikes like = FreeBoardLikes.builder()
                .freeBoard(freeBoard)
                .member(member)
                .build();
        freeBoardLikesRepository.save(like);
    }

    public void unlikeFreeBoard(Long freeBoardId, Long memberId) {
        FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid freeBoard ID"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        FreeBoardLikes like = freeBoardLikesRepository.findByFreeBoardAndMember(freeBoard, member)
                .orElseThrow(() -> new IllegalStateException("Like not found"));
        freeBoardLikesRepository.delete(like);
    }
    
//    public Page<FreeBoardResponseDTO> pageFreeBoardByLikes(Pageable pageable) {
//        Page<FreeBoard> freeBoards = freeBoardRepository.findAllByLikesCount(pageable);
//        Member currentUser = getCurrentUser();
//        return freeBoards.map(freeBoard -> {
//            boolean likeState = freeBoardLikesRepository.existsByFreeBoardAndMember(freeBoard, currentUser);
//            int likeCount = freeBoard.getLikes().size();
//            int commentCount = freeBoard.getComments().size();
//            return FreeBoardResponseDTO.builder()
//                    .id(freeBoard.getId())
//                    .title(freeBoard.getTitle())
//                    .content(freeBoard.getContent())
//                    .createDate(freeBoard.getCreateDate())
//                    .updateDate(freeBoard.getUpdateDate())
//                    .imageList(freeBoard.getImageList())
//                    .likeState(likeState)
//                    .likeCount(likeCount)
//                    .commentCount(commentCount)
//                    .build();
//        });
//    }
    public Page<FreeBoardResponseDTO> pageFreeBoardByLikes(Pageable pageable, Long memberId) {
        Page<FreeBoard> freeBoards = freeBoardRepository.findAllByLikesCount(pageable);
        // 임시로 생성한 member 객체 사용
        Member currentUser = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. ID: " + memberId));
        
        return freeBoards.map(freeBoard -> {
            boolean likeState = freeBoardLikesRepository.existsByFreeBoardAndMember(freeBoard, currentUser);
            int likeCount = freeBoard.getLikes().size();
            int commentCount = freeBoard.getComments().size();
            return FreeBoardResponseDTO.builder()
                    .id(freeBoard.getId())
                    .title(freeBoard.getTitle())
                    .content(freeBoard.getContent())
                    .createDate(freeBoard.getCreateDate())
                    .updateDate(freeBoard.getUpdateDate())
                    .imageList(freeBoard.getImageList())
                    .likeState(likeState)
                    .likeCount(likeCount)
                    .commentCount(commentCount)
                    .build();
        });
    }
    
    public Page<FreeBoardResponseDTO> searchByTitleLike(String title, Pageable pageable) {
        Page<FreeBoard> freeBoards = freeBoardRepository.searchByTitleLike(title, pageable);
        Member currentUser = getCurrentUser1();
        return freeBoards.map(freeBoard -> {
            boolean likeState = freeBoardLikesRepository.existsByFreeBoardAndMember(freeBoard, currentUser);
            int likeCount = freeBoard.getLikes().size();
            int commentCount = freeBoard.getComments().size();
            return FreeBoardResponseDTO.builder()
                    .id(freeBoard.getId())
                    .title(freeBoard.getTitle())
                    .content(freeBoard.getContent())
                    .createDate(freeBoard.getCreateDate())
                    .updateDate(freeBoard.getUpdateDate())
                    .imageList(freeBoard.getImageList())
                    .likeState(likeState)
                    .likeCount(likeCount)
                    .commentCount(commentCount)
                    .build();
        });
    }
    private Member getCurrentUser1() {
        Long memberId = 1L; // 임시로 memberId 1을 사용
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalStateException("Member not found"));
    }
    
    public Page<FreeBoardResponseDTO> pageFreeBoardByOldest(Pageable pageable, Long memberId) {
        Page<FreeBoard> freeBoards = freeBoardRepository.pageFreeBoardByOldest(pageable);
        Member currentUser = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. ID: " + memberId));

        return freeBoards.map(freeBoard -> {
            boolean likeState = freeBoardLikesRepository.existsByFreeBoardAndMember(freeBoard, currentUser);
            int likeCount = freeBoard.getLikes().size();
            int commentCount = freeBoard.getComments().size();
            return FreeBoardResponseDTO.builder()
                    .id(freeBoard.getId())
                    .title(freeBoard.getTitle())
                    .content(freeBoard.getContent())
                    .createDate(freeBoard.getCreateDate())
                    .updateDate(freeBoard.getUpdateDate())
                    .imageList(freeBoard.getImageList())
                    .likeState(likeState)
                    .likeCount(likeCount)
                    .commentCount(commentCount)
                    .build();
        });
    }
    public Page<FreeBoardResponseDTO> getFreeBoardsByUser(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        Page<FreeBoard> freeBoards = freeBoardRepository.findAllByMember(member, pageable);

        return freeBoards.map(freeBoard -> {
            int likeCount = freeBoard.getLikes().size();
            int commentCount = freeBoard.getComments().size();
            return FreeBoardResponseDTO.builder()
                    .id(freeBoard.getId())
                    .title(freeBoard.getTitle())
                    .content(freeBoard.getContent())
                    .createDate(freeBoard.getCreateDate())
                    .updateDate(freeBoard.getUpdateDate())
                    .imageList(freeBoard.getImageList())
                    .likeState(false) // 기본값으로 설정
                    .likeCount(likeCount)
                    .commentCount(commentCount)
                    .build();
        });
    }
}