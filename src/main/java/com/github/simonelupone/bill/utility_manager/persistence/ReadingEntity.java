package com.github.simonelupone.bill.utility_manager.persistence;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "readings")
@Getter
@Setter
@NoArgsConstructor
public class ReadingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "reading_date", nullable = false, unique = true)
    private LocalDate readingDate;

    @Column(name = "kwh_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal kwhValue;
}
