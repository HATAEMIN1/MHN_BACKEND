package com.project.mhnbackend.payment.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UnScheduleRequestDTO {
    private Long memberId;
    private String customer_uid;
    private String merchant_uid;
}
