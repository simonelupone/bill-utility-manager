package com.github.simonelupone.bill.utility_manager.domain.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

/**
 * Represents a billing period defined by a start and an end date (inclusive).
 * <p>
 * This record models the validity range of an invoice and provides utility
 * methods to create periods based on standard bimesters.
 * </p>
 *
 * @param start the start date of the billing period (inclusive)
 * @param end   the end date of the billing period (inclusive)
 */
public record BillPeriod(LocalDate start, LocalDate end) {

    /**
     * Compact constructor for validation.
     *
     * @param start start date of the billing period
     * @param end   end date of the billing period
     * @throws NullPointerException     if {@code start} or {@code end} is
     *                                  {@code null}
     * @throws IllegalArgumentException if {@code end} is before {@code start}
     */
    public BillPeriod {
        Objects.requireNonNull(start, "Start date must not be null");
        Objects.requireNonNull(end, "End date must not be null");

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date (" + end + ") cannot be before start date (" + start + ")");
        }
    }

    /**
     * Creates a new {@code BillPeriod} with the specified dates.
     *
     * @param start start date of the billing period
     * @param end   end date of the billing period
     * @return a new {@code BillPeriod} instance
     * @throws NullPointerException     if {@code start} or {@code end} is
     *                                  {@code null}
     * @throws IllegalArgumentException if {@code end} is before {@code start}
     */
    public static BillPeriod of(LocalDate start, LocalDate end) {
        return new BillPeriod(start, end);
    }

    /**
     * Creates a {@code BillPeriod} corresponding to a specific bimester of a year.
     * <p>
     * A bimester is defined as a two-month period:
     * <ul>
     * <li>1 = January–February</li>
     * <li>2 = March–April</li>
     * <li>3 = May–June</li>
     * <li>4 = July–August</li>
     * <li>5 = September–October</li>
     * <li>6 = November–December</li>
     * </ul>
     * </p>
     *
     * @param bimesterIndex index of the bimester (must be between 1 and 6)
     * @param year          the reference year
     * @return a {@code BillPeriod} covering the specified bimester
     * @throws IllegalArgumentException if {@code bimesterIndex} is not in the range
     *                                  1–6
     */
    public static BillPeriod ofBimester(int bimesterIndex, int year) {
        if (bimesterIndex < 1 || bimesterIndex > 6) {
            throw new IllegalArgumentException("Bimester index must be between 1 and 6. Got: " + bimesterIndex);
        }

        int startMonth = (bimesterIndex * 2) - 1;
        int endMonth = bimesterIndex * 2;

        LocalDate start = LocalDate.of(year, startMonth, 1);
        LocalDate end = YearMonth.of(year, endMonth).atEndOfMonth();

        return new BillPeriod(start, end);
    }

    /**
     * Checks if a given date falls within this billing period.
     *
     * @param date the date to check
     * @return {@code true} if the date is equal to start, equal to end, or between
     *         them; {@code false} otherwise
     * @throws NullPointerException if {@code date} is {@code null}
     */
    public boolean contains(LocalDate date) {
        Objects.requireNonNull(date, "Date to check cannot be null");
        return !date.isBefore(start) && !date.isAfter(end);
    }
}
