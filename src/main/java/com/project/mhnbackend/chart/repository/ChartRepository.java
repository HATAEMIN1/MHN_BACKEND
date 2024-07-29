package com.project.mhnbackend.chart.repository;

import com.project.mhnbackend.chart.domain.MedicalChart;
import com.project.mhnbackend.chart.dto.response.ChartResponseDTO;
import com.project.mhnbackend.chart.dto.response.ChartViewResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChartRepository extends JpaRepository<MedicalChart,Long> {
    @Query("SELECT new com.project.mhnbackend.chart.dto.response.ChartResponseDTO(" +
            "mc.id," +
            "mcmci.fileName, " +
            "m.nickName, " +
            "p.name, " +
            "p.kind, " +
            "mc.diagnosis, " +
            "mc.createdAt) " +
            "FROM MedicalChart mc " +
            "JOIN mc.medicalChartImage mcmci " +
            "JOIN mc.pet p " +
            "JOIN p.member m " +
            "WHERE mcmci.ord=0 AND m.id=:id")
    List<ChartResponseDTO> findMedicalCharts(@Param("id") Long memberId);

}
