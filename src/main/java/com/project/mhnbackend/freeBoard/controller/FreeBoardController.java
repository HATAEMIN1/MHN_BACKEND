package com.project.mhnbackend.freeBoard.controller;

import com.project.mhnbackend.freeBoard.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FreeBoardController {

    private final FreeBoardService freeBoardService;

}