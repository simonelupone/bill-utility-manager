package com.github.simonelupone.bill.utility_manager.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents the complete Electricity Bill (Bolletta).
 * <p>
 * This record acts as the <strong>Aggregate Root</strong> of the billing
 * domain, grouping together:
 * <ul>
 * <li>Metadata (Invoice Number)</li>
 * <li>Time validity (Period)</li>
 * <li>Consumption data (Total kWh)</li>
 * <li>Economic breakdown (Charges)</li>
 * </ul>
 * </p>
 *
 * @param invoiceNumber unique identifier of the invoice (e.g., "2023001589")
 * @param period        the time range this bill covers
 * @param totalKwh      total energy consumed in kWh (must be non-negative)
 * @param charges       detailed breakdown of costs
 */
public record Bill(
        String invoiceNumber,
        BillPeriod period,
        BigDecimal totalKwh,
        BillCharges charges) {

    /**
     * Compact constructor for validation.
     *
     * @param invoiceNumber unique identifier
     * @param period        billing period
     * @param totalKwh      total consumption
     * @param charges       cost breakdown
     * @throws NullPointerException     if any field is null
     * @throws IllegalArgumentException if {@code totalKwh} is negative
     */
    public Bill {
        Objects.requireNonNull(invoiceNumber, "Invoice number cannot be null");

        if (invoiceNumber.isBlank()) {
            throw new IllegalArgumentException("Invoice number cannot be empty or blank");
        }

        Objects.requireNonNull(period, "Bill period cannot be null");
        Objects.requireNonNull(totalKwh, "Total kWh cannot be null");
        Objects.requireNonNull(charges, "Bill charges breakdown cannot be null");

        if (totalKwh.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total kWh cannot be negative. Got: " + totalKwh);
        }
    }

    /**
     * Convenience method to get the final total amount to pay.
     * Delegates the calculation to the underlying {@link BillCharges}.
     *
     * @return the total amount in Euro
     */
    public BigDecimal getTotalAmount() {
        return charges.getTotalAmount();
    }
}