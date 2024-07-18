package com.project.mhnbackend.subscription.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/api/v1")
@RestController
public class PaymentController {
    private final IamportClient iamportClient;
    public PaymentController(@Value("${iamport.api.key}") String apiKey,
                             @Value("${iamport.api.secret}") String apiSecret) {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
    }
    @ResponseBody
    @GetMapping("/verify/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid)  //가맹점 식별코드 인자
            throws IamportResponseException, IOException {
        // 데이터와 처음 금액이 일치 확인 이후 결제 성공 실패 여부 리턴
        System.out.println("결제 검증 서비스 실행");
        return iamportClient.paymentByImpUid(imp_uid);
    }
}
