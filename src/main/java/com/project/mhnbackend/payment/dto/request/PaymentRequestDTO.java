package com.project.mhnbackend.payment.dto.request;

import lombok.Getter;


@Getter
public class PaymentRequestDTO {
    private Long userId;
    private String pg;
    private String merchantUid;
    private String impUid;
    private String email;  //주문자 이메일
    private int amount;
    private String buyerTel;
}
