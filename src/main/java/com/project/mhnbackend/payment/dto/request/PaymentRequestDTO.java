package com.project.mhnbackend.payment.dto.request;

import lombok.Getter;


@Getter
public class PaymentRequestDTO {
    private Long userId;
    private String pg;
    private String merchantUid;
    private String customerUid;
    private String impUid;
    private String productName;
    private String buyerName;
    private int amount;
    private String buyerTel;

}
