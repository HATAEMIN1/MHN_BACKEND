package com.project.mhnbackend.chart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChartResponseDTO {
    private Long id;
    private String imgUrl;
    private String userName;
    private String petName;
    private String petKind;
    private String diagnosis;
    private LocalDateTime createdAt;
}
