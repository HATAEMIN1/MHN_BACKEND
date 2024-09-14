package com.project.mhnbackend.chart.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChartResponseDTO implements Serializable {
    private Long id;
    private String imgUrl;
    private String userName;
    private String petName;
    private String petKind;
    private String diagnosis;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime createdAt;
}
