package com.project.mhnbackend.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.member.domain.MemberType;
import com.project.mhnbackend.member.repository.MemberRepository;
import com.project.mhnbackend.pet.domain.Pet;
import com.project.mhnbackend.pet.domain.PetImage;
import com.project.mhnbackend.pet.repository.PetRepository;

import java.time.LocalDate;
import java.util.Random;

@SpringBootTest
public class DataGenerationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Random random = new Random();

    @Test
    @Transactional
    @Rollback(false)  // This will prevent transaction rollback
    public void testInsertMemberAndPetData() {
        for (int i = 0; i < 30; i++) {
            Member member = createMember(i);
            memberRepository.save(member);

            int petCount = random.nextInt(3) + 1;
            for (int j = 0; j < petCount; j++) {
                Pet pet = createPet(member, j);
                petRepository.save(pet);
            }
        }
    }

    private Member createMember(int i) {
        return Member.builder()
                .email("user" + i + "@example.com")
                .password(passwordEncoder.encode("password" + i))
                .name(generateValidName(i))
                .nickName("Nick" + i)
                .tel("01012345" + String.format("%03d", i))
                .profileImageUrl("http://example.com/profile" + i + ".jpg")
                .build();
    }

    private String generateValidName(int i) {
        String baseName = "User" + i;
        return baseName.substring(0, Math.min(baseName.length(), 5));
    }

    private Pet createPet(Member member, int j) {
        return Pet.builder()
                .member(member)
                .name("Pet" + j)
                .kind(getRandomPetKind())
                .age(LocalDate.now().minusYears(random.nextInt(15)))
                .build();
    }

    private String getRandomPetKind() {
        String[] kinds = {"Dog", "Cat", "Bird", "Fish"};
        return kinds[random.nextInt(kinds.length)];
    }
}