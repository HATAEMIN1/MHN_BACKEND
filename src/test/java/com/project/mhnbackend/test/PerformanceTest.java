package com.project.mhnbackend.test;


import com.project.mhnbackend.test.chart.MedicalChartDataTest;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PerformanceTest {
    @Autowired
    private MedicalChartDataTest medicalChartDataTest;

    @Test
    public void insertTestData() {
        medicalChartDataTest.generateTestData();
        System.out.println("Test data insertion completed.");
    }
}