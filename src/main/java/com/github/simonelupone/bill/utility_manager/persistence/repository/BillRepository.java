package com.github.simonelupone.bill.utility_manager.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.simonelupone.bill.utility_manager.persistence.BillEntity;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Long> {
    Optional<BillEntity> findByInvoiceNumber(String invoiceNumber);
}
