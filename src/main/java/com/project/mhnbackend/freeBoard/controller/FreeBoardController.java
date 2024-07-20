package com.project.mhnbackend.freeBoard.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.project.mhnbackend.common.exception.CMRespDTO;
import com.project.mhnbackend.common.util.FileUploadUtil;
import com.project.mhnbackend.freeBoard.domain.FreeBoard;
import com.project.mhnbackend.freeBoard.dto.request.EditFreeBoardDTO;
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
    
    @GetMapping("/boards")
    public ResponseEntity<?> freeBoardList(@PageableDefault(size = 4) Pageable pageable) {
        Page<Object[]> freeBoards = freeBoardService.pageFreeBoard(pageable);
        return new ResponseEntity<>(new CMRespDTO<>(1, "성공", freeBoards), HttpStatus.OK);
    }
    
    @GetMapping("/boards/view")
    public FreeBoard getFreeBoard(@RequestParam("freeBoardId") Long freeBoardId) {
    	return freeBoardService.getFreeBoard(freeBoardId);
    }
    
    @PutMapping("/boards/view")
    public void editFreeBoard(
    		@RequestParam("freeBoardId") Long freeBoardId,
    		@RequestBody EditFreeBoardDTO editFreeBoardDTO
    		) {
    	freeBoardService.editFreeBoard(freeBoardId, editFreeBoardDTO);
    }
    
    @DeleteMapping("/boards/view")
    public void deleteFreeBoard(@RequestParam("freeBoardId") Long freeBoardId) {
    	freeBoardService.deleteFreeBoard(freeBoardId);
    }
   
}