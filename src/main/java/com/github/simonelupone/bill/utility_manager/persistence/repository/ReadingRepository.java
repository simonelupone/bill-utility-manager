package com.github.simonelupone.bill.utility_manager.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.simonelupone.bill.utility_manager.persistence.ReadingEntity;
import java.time.LocalDate;

@Repository
public interface ReadingRepository extends JpaRepository<ReadingEntity, Long> {
    Optional<ReadingEntity> findByReadingDate(LocalDate date);
}
