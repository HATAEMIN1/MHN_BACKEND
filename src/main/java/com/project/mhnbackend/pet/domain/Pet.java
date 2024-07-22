package com.project.mhnbackend.pet.domain;


import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.project.mhnbackend.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "pet")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class Pet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
//	private Integer user_id;
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
	private String name;
	private String kind;
//	private int age;
	private LocalDate age;
	private String petImage;
}