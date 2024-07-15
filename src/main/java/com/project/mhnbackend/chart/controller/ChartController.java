package com.project.mhnbackend.chart.controller;

import com.project.mhnbackend.chart.dto.request.ChartRequestDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class ChartController {
    @PostMapping("/charts")
    public String createCharts(ChartRequestDTO requestDTO){
        return "OK";
    }
}
