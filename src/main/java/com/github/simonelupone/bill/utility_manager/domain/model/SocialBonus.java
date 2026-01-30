package com.github.simonelupone.bill.utility_manager.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a social bonus or state subsidy applied to the bill.
 * <p>
 * This record models the bonus as a negative monetary value, representing a
 * credit
 * that reduces the total amount to be paid.
 * </p>
 *
 * @param months the number of months the bonus covers (must be positive)
 * @param amount the total amount of the bonus (must be negative, e.g., -45.00)
 */
public record SocialBonus(int months, BigDecimal amount) {

    /**
     * Compact constructor for validation.
     *
     * @param months number of months
     * @param amount monetary amount
     * @throws IllegalArgumentException if {@code months} is not positive or
     *                                  {@code amount} is not negative
     * @throws NullPointerException     if {@code amount} is {@code null}
     */
    public SocialBonus {
        if (months <= 0) {
            throw new IllegalArgumentException("Months must be a positive integer. Got: " + months);
        }

        Objects.requireNonNull(amount, "Bonus amount cannot be null");

        // Enforce strict negative value logic (0 is not a bonus, positive is a charge)
        if (amount.compareTo(BigDecimal.ZERO) >= 0) {
            throw new IllegalArgumentException(
                    "Social Bonus amount must be negative (e.g., -50.00) to represent a credit. Got: " + amount);
        }
    }

    /**
     * Creates a {@code SocialBonus} from a string representation of the amount.
     *
     * @param months    number of months
     * @param amountStr string representation of the amount (e.g., "-50.25")
     * @return a new {@code SocialBonus} instance
     * @throws NumberFormatException    if {@code amountStr} is not a valid number
     * @throws IllegalArgumentException if validation fails
     */
    public static SocialBonus of(int months, String amountStr) {
        Objects.requireNonNull(amountStr, "Amount string cannot be null");
        return new SocialBonus(months, new BigDecimal(amountStr));
    }
}