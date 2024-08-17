package com.project.mhnbackend.hospital.domain;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.mhnbackend.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "hospital_appointment", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"appointmentDateTime", "hospital_id"})
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HospitalAppointment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private LocalDateTime appointmentDateTime;
	
//	@ManyToOne
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;
	
//	@ManyToOne
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hospital_id", nullable = false)
	private Hospital hospital;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	@Setter
	// null값 말고 기본으로 생성해주는거인듯?
	private AppointmentStatus status = AppointmentStatus.PENDING;
	
	@Column(nullable = false)
	private LocalDateTime createdAt;
	
//	@Column(nullable = false)
	private LocalDateTime updatedAt;
	
	// Simplified Enum for AppointmentStatus
	public enum AppointmentStatus {
		PENDING,
		APPROVED,
		REJECTED
	}
}
