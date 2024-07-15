package com.project.mhnbackend.chart.repository;

import com.project.mhnbackend.chart.domain.MedicalChart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChartRepository extends JpaRepository<MedicalChart,Long> {
}
