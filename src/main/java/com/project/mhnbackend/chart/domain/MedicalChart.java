package com.project.mhnbackend.chart.domain;

import com.project.mhnbackend.pet.domain.Pet;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "medicalChartImage")
public class MedicalChart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;
    private String hospitalName;
    private String diagnosis;
    private String description;
    private String treatmentDate;
    private LocalDateTime createdAt;
    @ElementCollection
    @Builder.Default
    private List<MedicalChartImage> medicalChartImage = new ArrayList<>();

    public void addImage(MedicalChartImage image) {
        image.setOrd(this.medicalChartImage.size());
        medicalChartImage.add(image);
    }
    public void addImageString(String fileName){
        MedicalChartImage medicalChartImage = MedicalChartImage
                .builder()
                .fileName(fileName)
                .build();
        addImage(medicalChartImage);
    }

}
