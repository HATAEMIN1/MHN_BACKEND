package com.project.mhnbackend.chart.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class ChartRequestDTO {
    private Long petId;
    private String hospitalName;
    private String petName;
    private String visitDate;
    private String diseaseName;
    private String description;

    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();

//    public void setUploadFileNames(List<String> uploadFileNames) {
//        this.uploadFileNames = uploadFileNames;
//    }
}
