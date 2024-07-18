package com.project.mhnbackend.freeBoard.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.project.mhnbackend.common.util.FileUploadUtil;
import com.project.mhnbackend.freeBoard.domain.FreeBoard;
import com.project.mhnbackend.freeBoard.dto.request.FreeBoardRequestDTO;
import com.project.mhnbackend.freeBoard.dto.response.FreeBoardResponseDTO;
import com.project.mhnbackend.freeBoard.service.FreeBoardService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FreeBoardController {

	@Autowired
    private FreeBoardService freeBoardService;
	@Autowired
    private FileUploadUtil fileUploadUtil;
    
    @PostMapping("/boards")
    public Map<String, Long> registerFreeBoard(FreeBoardRequestDTO freeBoardRequestDTO){
    	List<MultipartFile> files = freeBoardRequestDTO.getFiles();
    	List<String> uploadFileNames = fileUploadUtil.saveFiles(files);
    	
    	freeBoardRequestDTO.setUploadFileNames(uploadFileNames);
    	
    	Long id = freeBoardService.register(freeBoardRequestDTO);
    	return Map.of("Result", id);
    }
}