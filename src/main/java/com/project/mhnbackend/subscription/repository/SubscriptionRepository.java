package com.project.mhnbackend.subscription.repository;

import com.project.mhnbackend.subscription.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    List<Subscription> findByNextBillingDateBeforeAndStatusIn(LocalDateTime now, List<Subscription.SubscriptionStatus> statuses);

    Optional<Subscription> findByMemberId(Long memberId);
}
