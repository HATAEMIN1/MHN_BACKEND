package com.project.mhnbackend.doctor.domain;

import com.project.mhnbackend.hospital.domain.Hospital;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Doctor {
//	id , hospital_id, eamil.password, status,createdAt
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name="hospital_id")
	private Hospital hospital;
	
	private String email;
	private String password;
	
	private LocalDateTime createdAt;
	
	@Enumerated(EnumType.STRING)
	private DoctorStatus doctorStatus;
	
	public enum DoctorStatus {
		FULFILLED , PENDING
	}
}
