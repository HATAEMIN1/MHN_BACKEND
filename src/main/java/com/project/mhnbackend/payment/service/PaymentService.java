package com.project.mhnbackend.payment.service;

import com.project.mhnbackend.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;


    public void getPayment(String billingKey) {
//        paymentRepository.findBybillingkey(billingKey);
    }
}
