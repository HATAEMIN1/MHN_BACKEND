package com.project.mhnbackend.member.domain;




import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member { // member is at the top most level i.e. Member is the master table

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String nickName;

    private String name;

    private String tel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberType memberType;

    @Column(nullable = false, updatable = false)

    private ZonedDateTime createdAt;

    @Column(nullable = false)
    private ZonedDateTime updatedAt;
}
