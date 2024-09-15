package com.project.mhnbackend.test.chart;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.project.mhnbackend.member.repository.MemberRepository;
import com.project.mhnbackend.pet.repository.PetRepository;
import com.project.mhnbackend.chart.domain.MedicalChart;
import com.project.mhnbackend.chart.repository.ChartRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest
public class ChartDataGenerationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ChartRepository chartRepository;

    private Random random = new Random();

    @Test
    @Transactional
    @Rollback(false)
    public void testInsertChartData() {
        long[] memberIds = IntStream.rangeClosed(42, 71).mapToLong(i -> i).toArray();
        long[] petIds = IntStream.rangeClosed(86, 148).mapToLong(i -> i).toArray();

        for (int i = 0; i < 100; i++) { // 100개의 차트 데이터 생성
            long memberId = memberIds[random.nextInt(memberIds.length)];
            long petId = petIds[random.nextInt(petIds.length)];

            MedicalChart chart = createChart(memberId, petId);
            chartRepository.save(chart);
        }
    }

    private MedicalChart createChart(long memberId, long petId) {
        return MedicalChart.builder()
                .pet(petRepository.findById(petId).orElseThrow())
                .hospitalName("Hospital " + random.nextInt(10))
                .diagnosis(getRandomDiagnosis())
                .description("Description for diagnosis: " + getRandomDiagnosis())
                .treatmentDate(LocalDateTime.now().minusDays(random.nextInt(365)).format(DateTimeFormatter.ISO_DATE))
                .createdAt(LocalDateTime.now())
                .build();
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