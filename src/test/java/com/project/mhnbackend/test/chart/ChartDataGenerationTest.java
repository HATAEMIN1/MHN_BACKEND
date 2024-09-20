package com.project.mhnbackend.test.chart;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.project.mhnbackend.pet.repository.PetRepository;
import com.project.mhnbackend.chart.domain.MedicalChart;
import com.project.mhnbackend.chart.repository.ChartRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Rollback(false)
public class ChartDataGenerationTest {

    @Autowired
    private ChartRepository chartRepository;

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private EntityManager entityManager;

    private Random random = new Random();

    @Test
    public void testInsertChartData() {
        long[] memberIds = IntStream.rangeClosed(134, 163).mapToLong(i -> i).toArray();
        long[] petIds = IntStream.rangeClosed(3, 58).mapToLong(i -> i).toArray();

        for (int i = 0; i < 100; i++) {
            long memberId = memberIds[random.nextInt(memberIds.length)];
            long petId = petIds[random.nextInt(petIds.length)];

            MedicalChart chart = createChart(memberId, petId);
            chartRepository.save(chart);
        }

        entityManager.flush();
        entityManager.clear();

        // 데이터 삽입 확인
        var charts = chartRepository.findAll();
        assertFalse(charts.isEmpty());
        assertEquals(100, charts.size());

        // 이미지 추가 확인
        charts.forEach(chart -> {
            entityManager.refresh(chart);  // 엔티티를 새로고침하여 관계를 로드
            assertFalse(chart.getMedicalChartImage().isEmpty());
            assertTrue(chart.getMedicalChartImage().get(0).getOrd() == 0);
        });
    }

    private MedicalChart createChart(long memberId, long petId) {
        MedicalChart chart = MedicalChart.builder()
                .pet(petRepository.findById(petId).orElseThrow())
                .hospitalName("Hospital " + random.nextInt(10))
                .diagnosis(getRandomDiagnosis())
                .description("Description for diagnosis: " + getRandomDiagnosis())
                .treatmentDate(LocalDateTime.now().minusDays(random.nextInt(365)).format(DateTimeFormatter.ISO_DATE))
                .createdAt(LocalDateTime.now())
                .build();

        // 이미지 추가
        chart.addImageString("testImage_" + random.nextInt(1000) + ".jpg");

        return chart;
    }

    private String getRandomDiagnosis() {
        String[] diagnoses = {
                "Common Cold", "Ear Infection", "Skin Allergy", "Dental Disease",
                "Urinary Tract Infection", "Arthritis", "Obesity", "Diarrhea",
                "Eye Infection", "Parasites"
        };
        return diagnoses[random.nextInt(diagnoses.length)];
    }
}