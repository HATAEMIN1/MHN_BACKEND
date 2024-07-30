package com.project.mhnbackend.pet.controller;



import java.util.List;

import com.project.mhnbackend.pet.dto.response.PetResponseDTO;
import com.project.mhnbackend.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.mhnbackend.common.util.PetFileUploadUtil;
import com.project.mhnbackend.pet.domain.Pet;
import com.project.mhnbackend.pet.dto.request.PetRequestDTO;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    // 펫 등록하는거
    @PostMapping("/pets")
    public Pet createPet(
             PetRequestDTO petRequestDTO
    ) {
        return petService.createPet(petRequestDTO);
    }

    // 펫 삭제하는거
    @DeleteMapping("/pets")
    public String deletePet(@RequestParam("petId") Long id) {
        return petService.deletePet(id);
    }

    
    // 모든 펫 리스트 겟
    @GetMapping("/pets")
    public List<PetResponseDTO> getAllPets(@RequestParam("memberId") Long memberId) {
        return petService.getAllPet(memberId)  ;
    }
    
}