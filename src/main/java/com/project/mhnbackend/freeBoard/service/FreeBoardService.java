package com.project.mhnbackend.freeBoard.service;

import com.project.mhnbackend.common.util.FileUploadUtil;
import com.project.mhnbackend.freeBoard.domain.BoardImage;
import com.project.mhnbackend.freeBoard.domain.FreeBoard;
import com.project.mhnbackend.freeBoard.dto.request.EditFreeBoardDTO;
import com.project.mhnbackend.freeBoard.dto.request.FreeBoardRequestDTO;
import com.project.mhnbackend.freeBoard.dto.response.FreeBoardResponseDTO;
import com.project.mhnbackend.freeBoard.repository.FreeBoardRepository;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Long register(FreeBoardRequestDTO freeBoardRequestDTO) {
    	FreeBoard freeBoard = dtoToEntity(freeBoardRequestDTO);
    	
    	Long id = freeBoardRepository.save(freeBoard).getId();
    	return id;
    }


	private FreeBoard dtoToEntity(FreeBoardRequestDTO freeBoardRequestDTO) {
		FreeBoard freeBoard = FreeBoard.builder()
				.title(freeBoardRequestDTO.getTitle())
				.content(freeBoardRequestDTO.getContent())
				.build();
		List<String> upLoadFileNames = freeBoardRequestDTO.getUploadFileNames();
		if (upLoadFileNames == null || upLoadFileNames.isEmpty()) {
			return freeBoard;
		}
		upLoadFileNames.forEach(fileName -> {
			freeBoard.addImageString(fileName);
		});
		
		return freeBoard;
	}
	
	
    public Page<Object[]> pageFreeBoard(Pageable pageable) {
        Page<Object[]> freeBoards = freeBoardRepository.pageFreeBoard(pageable);
        return freeBoards;
    }
    
    public FreeBoard getFreeBoard(Long freeBoardId) {
    	FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId).orElseThrow();
    	return FreeBoard.builder()
    			.title(freeBoard.getTitle())
    			.content(freeBoard.getContent())
    			.createDate(freeBoard.getCreateDate())
    			.updateDate(freeBoard.getUpdateDate())
    			.imageList(freeBoard.getImageList())
    			.build();
    }
    
    public void editFreeBoard(Long freeBoardId, EditFreeBoardDTO editFreeBoardDTO) {
    	FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId)
    			.orElseThrow(()->new RuntimeException("게시판 아이디가 없음"));
    	freeBoard.changeTitleAndContent(editFreeBoardDTO.getTitle(), editFreeBoardDTO.getContent());
    	freeBoardRepository.save(freeBoard);
    	
//    	List<String> uploadFileNames = editFreeBoardDTO.getUploadFileNames();
//    	if(uploadFileNames != null && uploadFileNames.size() > 0) {
//    		uploadFileNames.stream().forEach(
//					uploadName -> {
//						freeBoard.addImageString(uploadName);
//					});
//		}
//		freeBoardRepository.save(freeBoard);
    	}
    
    public void deleteFreeBoard(Long freeBoardId) {
    	FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId)
    			.orElseThrow(()->new RuntimeException("게시판 아이디가 없음"));
    	freeBoardRepository.delete(freeBoard);
    }
    
    
    }

