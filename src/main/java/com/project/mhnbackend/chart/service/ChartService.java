package com.project.mhnbackend.chart.service;

import com.project.mhnbackend.chart.domain.MedicalChart;
import com.project.mhnbackend.chart.dto.request.ChartRequestDTO;
import com.project.mhnbackend.chart.dto.response.ChartResponseDTO;
import com.project.mhnbackend.chart.dto.response.ChartViewResponseDTO;

import java.util.List;

public interface ChartService {

    MedicalChart createChart(ChartRequestDTO chartRequestDTO);

    List<ChartResponseDTO> getCharts(Long memberId);


    ChartViewResponseDTO getViewChart(Long id);

    ChartViewResponseDTO updateViewChart(ChartRequestDTO chartRequestDTO,Long id);
}
