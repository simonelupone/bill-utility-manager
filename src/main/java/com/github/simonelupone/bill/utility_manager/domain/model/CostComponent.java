package com.github.simonelupone.bill.utility_manager.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a specific cost component within a bill, defined by a total amount
 * and an optional unit price.
 * <p>
 * This record is used to model monetary values with precision, avoiding
 * floating-point errors by strictly using {@link BigDecimal}.
 * </p>
 *
 * @param amount    the total monetary amount (e.g., cost in Euro); must not be
 *                  {@code null}
 * @param unitPrice the unit price applied (e.g., Euro/kWh or Euro/month); may
 *                  be {@code null}
 */
public record CostComponent(BigDecimal amount, BigDecimal unitPrice) {

    /**
     * Compact constructor for validation.
     *
     * @param amount    the total monetary amount
     * @param unitPrice the unit price
     * @throws NullPointerException if {@code amount} is {@code null}
     */
    public CostComponent {
        Objects.requireNonNull(amount, "Amount cannot be null");
    }

    /**
     * Creates a {@code CostComponent} with both amount and unit price.
     *
     * @param amount    the total amount
     * @param unitPrice the unit price
     * @return a new {@code CostComponent}
     * @throws NullPointerException if {@code amount} is {@code null}
     */
    public static CostComponent of(BigDecimal amount, BigDecimal unitPrice) {
        return new CostComponent(amount, unitPrice);
    }

    /**
     * Creates a {@code CostComponent} with only the total amount (unit price is
     * null).
     *
     * @param amount the total amount
     * @return a new {@code CostComponent} with no unit price
     * @throws NullPointerException if {@code amount} is {@code null}
     */
    public static CostComponent ofAmount(BigDecimal amount) {
        return new CostComponent(amount, null);
    }

    /**
     * Creates a {@code CostComponent} from a String representation of the amount.
     * <p>
     * This is the preferred way to create instances from raw values to ensure
     * precision
     * (e.g., "0.1" instead of 0.1).
     * </p>
     *
     * @param amountStr the string representation of the amount
     * @return a new {@code CostComponent} with no unit price
     * @throws NullPointerException  if {@code amountStr} is {@code null}
     * @throws NumberFormatException if {@code amountStr} is not a valid
     *                               representation of a BigDecimal
     */
    public static CostComponent ofAmount(String amountStr) {
        Objects.requireNonNull(amountStr, "Amount string cannot be null");
        return new CostComponent(new BigDecimal(amountStr), null);
    }
}