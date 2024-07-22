package com.project.mhnbackend.payment.dto.response;

import lombok.Getter;

@Getter
public class ImpTokenResponseDTO {
    private int code;
    private String message;
    private TokenData response;

    @Getter
    public static class TokenData {
        private String access_token;
        private long expired_at;
        private long now;

    }


}
