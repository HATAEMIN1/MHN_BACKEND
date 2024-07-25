package com.project.mhnbackend.payment.repository;

import com.project.mhnbackend.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findTopByCustomerUidOrderByCreatedAtDesc(String customer_uid);

    Optional<Payment> findTopByMemberIdOrderByCreatedAtDesc(Long memberId);
}
