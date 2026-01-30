package com.github.simonelupone.bill.utility_manager.domain.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents the detailed breakdown of charges in a bill.
 * <p>
 * This record uses a "flattened" structure to simplify access to specific cost
 * components.
 * Fields that may not appear in every bill are wrapped in {@link Optional}.
 * </p>
 *
 * @param energyVariable      Cost component related to energy consumption
 *                            (Quota Energia variable)
 * @param transportFixed      Fixed cost component for transport and meter
 *                            management (Quota Fissa)
 * @param transportPowerQuota Cost component for power availability (Quota
 *                            Potenza)
 * @param exciseAndVat        Taxes and VAT (Accise e IVA)
 * @param tvTax               TV tax (Canone RAI). Pass {@link Optional#empty()}
 *                            if not present.
 * @param socialBonus         Social bonus (Bonus Sociale). Pass
 *                            {@link Optional#empty()} if not present.
 */
public record BillCharges(
        CostComponent energyVariable,
        CostComponent transportFixed,
        CostComponent transportPowerQuota,
        CostComponent exciseAndVat,
        Optional<BigDecimal> tvTax,
        Optional<SocialBonus> socialBonus) {

    /**
     * Compact constructor for validation.
     *
     * @throws NullPointerException if any field is null. Note: Optional fields must
     *                              be
     *                              passed as {@code Optional.empty()}, not
     *                              {@code null}.
     */
    public BillCharges {
        Objects.requireNonNull(energyVariable, "Energy variable cost cannot be null");
        Objects.requireNonNull(transportFixed, "Transport fixed cost cannot be null");
        Objects.requireNonNull(transportPowerQuota, "Power quota cost cannot be null");
        Objects.requireNonNull(exciseAndVat, "Excise and VAT cannot be null");

        // Correzione applicata: Messaggio pulito, istruzioni spostate nel Javadoc.
        Objects.requireNonNull(tvTax, "TV Tax container cannot be null");
        Objects.requireNonNull(socialBonus, "Social Bonus container cannot be null");
    }

    /**
     * Calculates the total amount of the bill by summing all components.
     * <p>
     * It automatically handles optional fields:
     * <ul>
     * <li>Adds TV Tax if present.</li>
     * <li>Adds Social Bonus if present (note: bonus amount is negative, so it
     * reduces the total).</li>
     * </ul>
     * </p>
     *
     * @return the total calculated amount of the bill
     */
    public BigDecimal getTotalAmount() {
        // Start with mandatory components
        BigDecimal total = energyVariable.amount()
                .add(transportFixed.amount())
                .add(transportPowerQuota.amount())
                .add(exciseAndVat.amount());

        // Add TV Tax if present (unwrapping the Optional safely)
        if (tvTax.isPresent()) {
            total = total.add(tvTax.get());
        }

        // Add Social Bonus if present
        if (socialBonus.isPresent()) {
            total = total.add(socialBonus.get().amount());
        }

        return total;
    }
}