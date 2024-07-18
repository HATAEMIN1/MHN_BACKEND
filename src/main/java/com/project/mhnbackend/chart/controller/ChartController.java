package com.project.mhnbackend.chart.controller;

import com.project.mhnbackend.chart.domain.MedicalChart;
import com.project.mhnbackend.chart.dto.request.ChartRequestDTO;
import com.project.mhnbackend.chart.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class ChartController {
    private final ChartService chartService;
    @PostMapping("/charts")
    public MedicalChart createCharts(ChartRequestDTO chartRequestDTO){
        return chartService.createChart(chartRequestDTO);
    }
}
