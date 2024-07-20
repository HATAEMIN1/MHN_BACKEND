package com.project.mhnbackend.chart.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MedicalChartImage {
    private String fileName;
    private int ord;
    public void setOrd(int ord) {
        this.ord = ord;
    }
}
