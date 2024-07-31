//package com.project.mhnbackend.member.domain;
//
//import java.time.ZonedDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.ElementCollection;
//import jakarta.persistence.Entity;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.PrePersist;
//import jakarta.persistence.PreUpdate;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.ToString;
//
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Table(name="member")
//@Getter
//@ToString(exclude = "memberTypeList")
//public class Member2 {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, unique = true)
//    private String email;
//
//    @Column(nullable = false)
//    private String password;
//
//    private String nickName;
//
//    private String name;
//
//    private String tel;
//
//    @Builder.Default
//    @ElementCollection
//    private List<MemberType> memberTypeList = new ArrayList<>();
//
//    @Column(nullable = false, updatable = false)
//    private ZonedDateTime createdAt;
//
//    @Column(nullable = false)
//    private ZonedDateTime updatedAt;
//
//    @PrePersist
//    protected void onCreate() {
//        createdAt = ZonedDateTime.now();
//        updatedAt = ZonedDateTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        updatedAt = ZonedDateTime.now();
//    }
//
//    public void addType(MemberType memberType) {
//        memberTypeList.add(memberType);
//    }
//
//    public void clearRoles() {
//        memberTypeList.clear();
//    }
//
//    public void changePassword(String password) {
//        this.password = password;
//    }
//
//    public void changeNickName(String nickName) {
//        this.nickName = nickName;
//    }
//}
