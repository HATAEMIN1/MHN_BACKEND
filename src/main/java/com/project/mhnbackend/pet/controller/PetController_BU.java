//package com.project.mhnbackend.pet.controller;
//
//
//import com.project.mhnbackend.common.util.PetFileUploadUtil;
//import com.project.mhnbackend.pet.domain.Pet;
//import com.project.mhnbackend.pet.dto.request.PetRequestDTO;
//import com.project.mhnbackend.pet.dto.response.PetResponseDTO;
//import com.project.mhnbackend.pet.service.PetService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.io.Resource;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//
//@RestController
//@RequestMapping("/api/v1")
//@RequiredArgsConstructor
//public class PetController_BU {
//
//    private final PetService petService;
//    private final PetFileUploadUtil fileUploadUtil;
//
//
//    // 모든 펫 가져오는거
//    @GetMapping("/pets/allpet")
//    public ResponseEntity<List<Pet>> getAllPet(Pet petentity) {
//        return petService.allPetGet(petentity);
//    }
//
//    // 펫 한마리 정보 가져오는거
////    @GetMapping("/{id}")
////    public ResponseEntity<Pet> getPetById(@PathVariable("id") Long id) {
////        return petService.getPetById(id);
////    }
//
//
//    // 펫 등록하는거
//    @PostMapping("/pets/addpet")
//    public ResponseEntity<String> savePet(@RequestBody PetRequestDTO dto) {
////        System.out.println("Received pet data: " + dto); // 디버깅용 로그
////        System.out.println("Received image: " + (dto.getPetImage() != null ? dto.getPetImage().getOriginalFilename() : "null")); // 디버깅용 로그
//        String result = petService.petSave(dto);
//        return ResponseEntity.ok(result);
//    }
//
//    // 이미지 가져오는 엔드포인트 추가
//    @GetMapping("/pets/image/{fileName:.+}")
//    public ResponseEntity<Resource> getPetImage(@PathVariable("fileName") String fileName) {
//        return fileUploadUtil.getFile(fileName);
//    }
//
//    // 펫 삭제하는거
//    @DeleteMapping("/pets/{id}")
//    public ResponseEntity<Void> deletePet(@PathVariable("id") Long id) {
//        return petService.deletePet(id);
//    }
//
//    @GetMapping("/pets")
//    public List<PetResponseDTO> getAllPets(@RequestParam Long memberId) {
//        return petService.getAllPet(memberId)  ;
//    }
//
//}