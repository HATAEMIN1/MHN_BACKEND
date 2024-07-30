package com.project.mhnbackend.pet.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PetImage {
	private String fileName;
	private int ord;
	public void setOrd(int ord) {
		this.ord = ord;
	}
}
