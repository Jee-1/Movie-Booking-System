package com.movieticket.booking.repository;

import com.movieticket.booking.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payments, Long> {

    // idempotency lookup: have we already processed this gateway reference?
    Optional<Payments> findByTransactionRef(String transactionRef);
}
