package com.project.mhnbackend.freeBoard.service;

import com.project.mhnbackend.common.util.FileUploadUtil;
import com.project.mhnbackend.freeBoard.domain.BoardImage;
import com.project.mhnbackend.freeBoard.domain.FreeBoard;
import com.project.mhnbackend.freeBoard.dto.request.FreeBoardRequestDTO;
import com.project.mhnbackend.freeBoard.repository.FreeBoardRepository;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

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

}
