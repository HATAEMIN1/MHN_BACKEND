package com.project.mhnbackend.member.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberView {
    private Long id;
    private String email;
    private String password;
    private String nickName;
    private String fullName;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
