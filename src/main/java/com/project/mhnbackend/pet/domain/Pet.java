package com.project.mhnbackend.pet.domain;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
	private String name;
	private String kind;
//	private int age;
@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate age;
	
	
	@ElementCollection
	@Builder.Default
	private List<PetImage> petImage = new ArrayList<> ();
	
	public void addImage(PetImage image) {
		image.setOrd(this.petImage.size());
		petImage.add(image);
	}
	public void addImageString(String fileName){
		PetImage petImage = PetImage
				.builder()
				.fileName(fileName)
				.build();
		addImage(petImage);
	}
}