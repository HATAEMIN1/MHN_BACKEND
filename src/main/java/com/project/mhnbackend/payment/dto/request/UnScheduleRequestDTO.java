package com.project.mhnbackend.payment.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnScheduleRequestDTO {
    private Long memberId;
    private String customer_uid;
    private String merchant_uid;
}
