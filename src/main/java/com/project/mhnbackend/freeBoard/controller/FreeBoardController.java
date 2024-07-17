package com.project.mhnbackend.freeBoard.controller;

import com.project.mhnbackend.freeBoard.domain.FreeBoard;
import com.project.mhnbackend.freeBoard.dto.request.FreeBoardRequestDTO;
import com.project.mhnbackend.freeBoard.service.FreeBoardService;
import com.project.mhnbackend.user.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FreeBoardController {

    private final FreeBoardService freeBoardService;

}