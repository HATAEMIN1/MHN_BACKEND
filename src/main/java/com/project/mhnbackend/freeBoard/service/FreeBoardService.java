package com.project.mhnbackend.freeBoard.service;

import com.project.mhnbackend.common.util.FileUploadUtil;
import com.project.mhnbackend.freeBoard.domain.Board;
import com.project.mhnbackend.freeBoard.domain.BoardImage;
import com.project.mhnbackend.freeBoard.domain.BoardType;
import com.project.mhnbackend.freeBoard.domain.FreeBoard;
import com.project.mhnbackend.freeBoard.dto.request.FreeBoardRequestDTO;
import com.project.mhnbackend.freeBoard.repository.BoardRepository;
import com.project.mhnbackend.freeBoard.repository.FreeBoardRepository;
import com.project.mhnbackend.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FreeBoardService {

    private final FreeBoardRepository freeBoardRepository;
    private final BoardRepository boardRepository;
    private final FileUploadUtil fileUploadUtil;

    public FreeBoardService(FreeBoardRepository freeBoardRepository, BoardRepository boardRepository, FileUploadUtil fileUploadUtil) {
        this.freeBoardRepository = freeBoardRepository;
        this.boardRepository = boardRepository;
        this.fileUploadUtil = fileUploadUtil;
    }

    public FreeBoard create(FreeBoardRequestDTO freeBoardRequestDTO, Member member, List<MultipartFile> files) {
        List<String> uploadedFileNames = fileUploadUtil.saveFiles(files);
        List<BoardImage> images = uploadedFileNames.stream()
                .map(fileName -> BoardImage.builder().fileName(fileName).build())
                .collect(Collectors.toList());

        Board board = Board.builder()
                .boardType(BoardType.FREE)  // BoardType을 자유게시판으로 설정
                .createdAt(LocalDateTime.now())
                .build();

        // 이미지를 Board에 추가
        images.forEach(board::addImage);

        // Board를 먼저 저장
        boardRepository.save(board);

        FreeBoard freeBoard = FreeBoard.builder()
                .title(freeBoardRequestDTO.getTitle())
                .content(freeBoardRequestDTO.getContent())
                .createdAt(LocalDateTime.now())
                .member(member)
                .board(board)
                .build();

        // FreeBoard와 Board의 연관관계를 설정
        freeBoard.setBoard(board);

        return freeBoardRepository.save(freeBoard);
    }
}
