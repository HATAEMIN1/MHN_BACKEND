package com.project.mhnbackend.member.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAndEditMemberRequest {
    private String email;
    private String password;
    private String nickName;
    private String fullName;
}
