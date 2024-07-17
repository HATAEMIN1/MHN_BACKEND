package com.project.mhnbackend.member.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="member")
@Builder
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String nickName;
    private String fullName;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    @Builder.Default
    private MemberRole role = MemberRole.PET_OWNER;

    public void changePassword(String password) {
        this.password = password;
    }
    public void changeNickName(String nickName) {
        this.nickName = nickName;
    }
    public void changeFullName(String fullName) {
        this.fullName = fullName;
    }
    public void changeUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
