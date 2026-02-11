package com.github.simonelupone.bill.utility_manager.persistence;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bill_charges")
@Getter
@Setter
@NoArgsConstructor
public class BillChargesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "bill_id", nullable = false, unique = true)
    private BillEntity bill;

    // CostComponent energyvalue
    @Column(name = "energy_variable_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal energyVariableAmount;
    @Column(name = "energy_variable_unit_price", nullable = false, precision = 16, scale = 2)
    private BigDecimal energyVariableUnitPrice;

    // Costcomponent transportfixed
    @Column(name = "transport_fixed_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal transportFixedAmount;
    @Column(name = "transport_fixed_unit_price", precision = 16, scale = 10)
    private BigDecimal transportFixedUnitPrice;

    // Costcomponent powerquota
    @Column(name = "transport_power_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal transportPowerAmount;
    @Column(name = "transport_power_unit_price", precision = 16, scale = 10)
    private BigDecimal transportPowerUnitPrice;

    // costcomponent exciseandvat
    @Column(name = "excise_vat_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal exciseVatAmount;
    @Column(name = "excise_vat_unit_price", precision = 16, scale = 10)
    private BigDecimal exciseVatUnitPrice;

    // optional tvtax
    @Column(name = "tv_tax_amount", precision = 10, scale = 2)
    private BigDecimal tvTaxAmount;

    // socialbonus opt
    @Column(name = "social_bonus_amount", precision = 10, scale = 2)
    private BigDecimal socialBonusAmount;
    @Column(name = "social_bonus_months")
    private Integer socialBonusMonths;
}
