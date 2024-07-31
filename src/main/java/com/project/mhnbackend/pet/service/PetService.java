package com.project.mhnbackend.pet.service;

import com.project.mhnbackend.pet.domain.Pet;
import com.project.mhnbackend.pet.dto.request.PetRequestDTO;
import com.project.mhnbackend.pet.dto.response.PetResponseDTO;

import java.util.List;

public interface PetService {
	Pet createPet(PetRequestDTO petRequestDTO);
	
	List<PetResponseDTO> getAllPet (Long memberId);
	
	String deletePet (Long id);
}
