package com.project.mhnbackend.chart.controller;

import com.project.mhnbackend.chart.domain.MedicalChart;
import com.project.mhnbackend.chart.dto.request.ChartRequestDTO;
import com.project.mhnbackend.chart.dto.response.ChartResponseDTO;
import com.project.mhnbackend.chart.dto.response.ChartViewResponseDTO;
import com.project.mhnbackend.chart.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class ChartController {
    private final ChartService chartService;

    @PostMapping("/charts")
    public MedicalChart createCharts(@ModelAttribute ChartRequestDTO chartRequestDTO){
        return chartService.createChart(chartRequestDTO);
    }

    @GetMapping("/charts")
    public Page<ChartResponseDTO> getCharts(@RequestParam("memberId") Long memberId, @PageableDefault(size = 4, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        return chartService.getCharts(memberId,pageable);
    }

    @GetMapping("/charts/view")
    public @ResponseBody ChartViewResponseDTO viewChart(@RequestParam("id") Long id){
        return  chartService.getViewChart(id);
    }
    @PutMapping("/charts/view")
    public ChartViewResponseDTO updateViewChart(ChartRequestDTO chartRequestDTO, @RequestParam("id") Long id){
        return chartService.updateViewChart(chartRequestDTO,id);
    }
    @DeleteMapping("/charts/view")
    public String deleteViewChart(@RequestParam("id") Long id){
        return chartService.deleteViewChart(id);
    }

}
