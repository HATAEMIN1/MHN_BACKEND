package com.project.mhnbackend.test.chart;

import com.project.mhnbackend.chart.domain.MedicalChart;
import com.project.mhnbackend.chart.repository.ChartRepository;
import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.member.domain.MemberType;
import com.project.mhnbackend.member.repository.MemberRepository;
import com.project.mhnbackend.pet.domain.Pet;
import com.project.mhnbackend.pet.repository.PetRepository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@Component
@RequiredArgsConstructor
public class MedicalChartDataTest {

    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final ChartRepository chartRepository;

    @Transactional
    public void generateTestData() {
        for (int i = 1; i <= 100; i++) {
            // Create a Member
            Member member = Member.builder()
                    .email("user" + i + "@example.com")
                    .password("password" + i)
                    .nickName("U" + i)
                    .name("Na" + i)
                    .tel("01012345678")  // 11자리 전화번호
                    .profileImageUrl("http://example.com/profile" + i + ".jpg")
                    .build();

            // Add a member type (예: USER)
            member.addType(MemberType.USER);

            memberRepository.save(member);

            // Create a Pet for the Member
            Pet pet = Pet.builder()
                    .name("Pet" + i)
                    .kind("Dog")
                    .member(member)
                    .build();
            petRepository.save(pet);

            // Create 10 MedicalCharts for the Pet
            for (int j = 1; j <= 10; j++) {
                MedicalChart chart = MedicalChart.builder()
                        .pet(pet)
                        .hospitalName("Hospital" + j)
                        .diagnosis("Diagnosis" + j)
                        .description("Description" + j)
                        .treatmentDate(LocalDate.now().minusDays(j).toString())
                        .createdAt(LocalDateTime.now().minusDays(j))
                        .build();

                chart.addImageString("image" + j + ".jpg");

                chartRepository.save(chart);
            }
        }
    }
}