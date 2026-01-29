package com.github.simonelupone.bill.utility_manager.domain.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Represents the readings for the second apartment (apt2). I add comparable for
 * comparison purposes.
 * 
 * Each instance corresponds to a single reading taken periodically.
 *
 */
public record Reading(LocalDate date, double kWh) implements Comparable<Reading> {

    /**
     * Creates a new {@code Reading}.
     * 
     * @param date date of the reading
     * @param kWh  energy consumption at that time
     * @throws NullPointerExeption     when date is null
     * @throws IllegalArgumentExeption when kWh are negative
     */
    public Reading {
        Objects.requireNonNull(date, "Date cannot be null");

        if (kWh < 0)
            throw new IllegalArgumentException("Consumption cannot be negative");
    }

    public static Reading of(LocalDate date, double kWh) {
        return new Reading(date, kWh);
    }

    /**
     * Parses a raw CSV-like string into a {@code Reading}.
     * Expected format: {@code YYYY-MM-DD,value}
     *
     * @param str raw string (e.g., "2023-10-01, 12500")
     * @return a valid Reading instance
     * @throws IllegalArgumentException if the string format is invalid
     */
    public static Reading parse(String str) {
        if (str == null)
            throw new IllegalArgumentException("Input string cannot be null");
        if (str.isBlank())
            throw new IllegalArgumentException("Input string cannot be empty or blank");

        String[] chunks = str.split(",", 2);

        if (chunks.length < 2)
            throw new IllegalArgumentException("Invalid format. Expected 'YYYY-MM-DD,value', got: " + str);

        try {
            LocalDate date = LocalDate.parse(chunks[0].trim());
            double consumption = Double.parseDouble(chunks[1].trim());

            return new Reading(date, consumption);
        } catch (DateTimeParseException | NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse: " + str, e);
        }
    }

    /**
     * Compares this reading with another based on the reading date.
     *
     * @param other the other reading to compare to
     * @return a negative value, zero, or a positive value if this reading
     *         is respectively before, equal to, or after the other
     */
    @Override
    public int compareTo(Reading other) {
        return this.date.compareTo(other.date);
    }
}
