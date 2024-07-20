package com.project.mhnbackend.chart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class ChartViewResponseDTO {
    private String hospitalName;
    private String petName;
    private String treatmentDate;
    private String diagnosis;
    private String description;
    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();

    public void setUploadFileNames(List<String> uploadFileNames) {
        this.uploadFileNames = uploadFileNames;
    }
}
