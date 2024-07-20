package com.project.mhnbackend.chart.service;

import com.project.mhnbackend.chart.domain.MedicalChart;
import com.project.mhnbackend.chart.dto.request.ChartRequestDTO;
import com.project.mhnbackend.chart.repository.ChartRepository;
import com.project.mhnbackend.common.util.FileUploadUtil;
import com.project.mhnbackend.pet.domain.Pet;
import com.project.mhnbackend.pet.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartServiceImpl implements ChartService {
    private final ChartRepository chartRepository;
    private final FileUploadUtil fileUploadUtil;
    private final PetRepository PetRepository;

    //파일 업로드 로직
    public List<String> uploadFile(ChartRequestDTO chartRequestDTO){
        List<MultipartFile> files = chartRequestDTO.getFiles();
        return fileUploadUtil.saveFiles(files);

    }

    @Override
    public MedicalChart createChart(ChartRequestDTO chartRequestDTO) {
        List<String> uploadFileNames = uploadFile(chartRequestDTO);

//        Pet pet = PetRepository.findById(chartRequestDTO.getPetId())
//                .orElseThrow(() -> new EntityNotFoundException("Pet not found"));

        MedicalChart medicalChart = MedicalChart.builder()
//                .pet(pet)
                .hospitalName(chartRequestDTO.getHospitalName())
                .diagnosis(chartRequestDTO.getDiseaseName())
                .treatmentDate(chartRequestDTO.getVisitDate())
                .description(chartRequestDTO.getDescription())
                .createdAt(LocalDateTime.now())
                .build();
        for (String fileName : uploadFileNames) {
            medicalChart.addImageString(fileName);
        }
        chartRepository.save(medicalChart);
        return medicalChart;
    }


}
