package com.project.mhnbackend.chart.service;

import com.project.mhnbackend.chart.domain.MedicalChart;
import com.project.mhnbackend.chart.dto.request.ChartRequestDTO;
import com.project.mhnbackend.chart.dto.response.ChartResponseDTO;
import com.project.mhnbackend.chart.dto.response.ChartViewResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChartService {

    MedicalChart createChart(ChartRequestDTO chartRequestDTO);

    Page<ChartResponseDTO> getCharts(Long memberId, Pageable pageable);


    ChartViewResponseDTO getViewChart(Long id);

    ChartViewResponseDTO updateViewChart(ChartRequestDTO chartRequestDTO,Long id);

    String deleteViewChart(Long id);
}
