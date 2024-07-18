package com.project.mhnbackend.chart.service;

import com.project.mhnbackend.chart.domain.MedicalChart;
import com.project.mhnbackend.chart.dto.request.ChartRequestDTO;

public interface ChartService {

    MedicalChart createChart(ChartRequestDTO chartRequestDTO);
}
