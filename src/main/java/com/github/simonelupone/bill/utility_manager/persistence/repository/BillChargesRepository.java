package com.github.simonelupone.bill.utility_manager.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.simonelupone.bill.utility_manager.domain.model.BillCharges;

@Repository
public interface BillChargesRepository extends JpaRepository<BillCharges, Long> {

}
