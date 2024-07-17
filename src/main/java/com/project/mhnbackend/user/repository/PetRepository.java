package com.project.mhnbackend.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.mhnbackend.user.domain.Pet;


@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    long count();

}