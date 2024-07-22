package com.project.mhnbackend.payment.dto.request;

import lombok.Getter;

@Getter
public class ImpTokenRequestDTO {
    private String imp_key;
    private String imp_secret;

    public ImpTokenRequestDTO(String imp_key, String imp_secret) {
        this.imp_key = imp_key;
        this.imp_secret = imp_secret;
    }
}
