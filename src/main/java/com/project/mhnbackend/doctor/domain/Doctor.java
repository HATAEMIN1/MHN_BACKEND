package com.project.mhnbackend.doctor.domain;

import com.project.mhnbackend.hospital.domain.Hospital;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name="doctor")
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
	
	public void changeDoctorStatus (DoctorStatus doctorStatus) {
		this.doctorStatus = doctorStatus;
	}
	
	public enum DoctorStatus {
		FULFILLED , PENDING
	}
}
