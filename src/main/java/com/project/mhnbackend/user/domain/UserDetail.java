package com.project.mhnbackend.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(length = 100)
    private String nickname;

    @Column(length = 100)
    private String name;

    @Column(length = 20)
    private String tel;

    @Column(length = 255)
    private String image;

    @OneToOne
    @MapsId
    @JoinColumn(name = "member_id")
    private Member member;
}
