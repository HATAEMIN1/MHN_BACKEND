package com.project.mhnbackend.pet.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.mhnbackend.pet.domain.Pet;

import java.util.List;
import java.util.Optional;


@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
//    long count();
//
    List<Pet> findByMemberId(Long memberId);
    Optional<Pet> findByName(String petName);
}