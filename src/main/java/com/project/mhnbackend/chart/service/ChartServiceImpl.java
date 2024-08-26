package com.project.mhnbackend.chart.service;

import com.project.mhnbackend.chart.domain.MedicalChart;
import com.project.mhnbackend.chart.domain.MedicalChartImage;
import com.project.mhnbackend.chart.dto.request.ChartRequestDTO;
import com.project.mhnbackend.chart.dto.response.ChartResponseDTO;
import com.project.mhnbackend.chart.dto.response.ChartViewResponseDTO;
import com.project.mhnbackend.chart.repository.ChartRepository;
import com.project.mhnbackend.common.util.FileUploadUtil;
import com.project.mhnbackend.pet.domain.Pet;
import com.project.mhnbackend.pet.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChartServiceImpl implements ChartService {
    private final ChartRepository chartRepository;
    private final FileUploadUtil fileUploadUtil;
    private final PetRepository petRepository;

    //파일 업로드 로직
    public List<String> uploadFile(ChartRequestDTO chartRequestDTO){
        List<MultipartFile> files = chartRequestDTO.getFiles();
        return fileUploadUtil.saveFiles(files);
    }
    @Transactional
    @Override
    public MedicalChart createChart(ChartRequestDTO chartRequestDTO) {
        List<String> uploadFileNames = uploadFile(chartRequestDTO);
        log.info("펫 아이디는 "+ chartRequestDTO.getPetId());
        Pet pet = petRepository.findById(chartRequestDTO.getPetId())
                .orElseThrow(() -> new EntityNotFoundException("Pet not found"));

        MedicalChart medicalChart = MedicalChart.builder()
                .pet(pet)
                .hospitalName(chartRequestDTO.getHospitalName())
                .diagnosis(chartRequestDTO.getDiseaseName())
                .treatmentDate(chartRequestDTO.getVisitDate())
                .description(chartRequestDTO.getDescription())
                .createdAt(LocalDateTime.now())
                .build();
        if(uploadFileNames == null){
            chartRepository.save(medicalChart);
            return medicalChart;
        }
        for (String fileName : uploadFileNames) {
            medicalChart.addImageString(fileName);
        }
        chartRepository.save(medicalChart);
        return medicalChart;
    }

    @Override
    public Page<ChartResponseDTO> getCharts(Long memberId, Pageable pageable) {
        return chartRepository.findMedicalCharts(memberId,pageable);
    }

    @Override
    public ChartViewResponseDTO getViewChart(Long id) {
        Optional<MedicalChart> chart = chartRepository.findById(id);
        Optional<Pet> pet = petRepository.findById(chart.get().getPet().getId());
        List<MedicalChartImage> imageList = chart.get().getMedicalChartImage();
        List<String> fileNameList = imageList.stream().map(
                MedicalChartImage -> MedicalChartImage.getFileName()
        ).toList();
        ChartViewResponseDTO responseDTO = ChartViewResponseDTO.builder()
                .hospitalName(chart.get().getHospitalName())
                .petName(pet.get().getName())
                .treatmentDate(chart.get().getTreatmentDate())
                .diagnosis(chart.get().getDiagnosis())
                .description(chart.get().getDescription())
                .build();
        responseDTO.setUploadFileNames(fileNameList);
        return responseDTO;
    }

    @Override
    public ChartViewResponseDTO updateViewChart(ChartRequestDTO chartRequestDTO,Long id) {
        Optional<MedicalChart> chart = chartRepository.findById(id);
//        List<MedicalChartImage> imageList = chart.get().getMedicalChartImage();
//        List<String> fileNameList = imageList.stream().map(
//                MedicalChartImage -> MedicalChartImage.getFileName()
//        ).toList();
//
        List<MedicalChartImage> imageList = chart.get().getMedicalChartImage();
        List<String> existingFileNames = imageList.stream()
                .map(MedicalChartImage->MedicalChartImage.getFileName())
                .collect(Collectors.toList());
        fileUploadUtil.deleteFiles(existingFileNames);

        Pet pet = petRepository.findByName(chartRequestDTO.getPetName())
                .orElseThrow(() -> new EntityNotFoundException("Pet not found"));

        chart.get().changeDiagnosis(chartRequestDTO.getDiseaseName());
        chart.get().changeDescription(chartRequestDTO.getDescription());
        chart.get().changeHospitalName(chartRequestDTO.getHospitalName());
        chart.get().changeTreatmentDate(chartRequestDTO.getVisitDate());
        chart.get().changeCreatedAt(LocalDateTime.now());

        List<String> uploadFileNames = uploadFile(chartRequestDTO);
        chart.get().getMedicalChartImage().clear();
        for (String fileName : uploadFileNames) {
            chart.get().addImageString(fileName);
        }
        chartRepository.save(chart.get());

        ChartViewResponseDTO responseDTO = ChartViewResponseDTO.builder()
                .hospitalName(chartRequestDTO.getHospitalName())
                .petName(pet.getName())
                .description(chartRequestDTO.getDescription())
                .treatmentDate(chartRequestDTO.getVisitDate())
                .diagnosis(chart.get().getDiagnosis())
                .build();
        responseDTO.setUploadFileNames(uploadFileNames);
        return  responseDTO;
    }
    @Transactional
    @Override
    public String deleteViewChart(Long id) {
        Optional<MedicalChart> chart = chartRepository.findById(id);
        List<MedicalChartImage> imageList = chart.get().getMedicalChartImage();
        List<String> existingFileNames = imageList.stream()
                .map(MedicalChartImage->MedicalChartImage.getFileName())
                .collect(Collectors.toList());
        fileUploadUtil.deleteFiles(existingFileNames);
        chartRepository.deleteById(id);
        return "삭제되었습니다";
    }


}
