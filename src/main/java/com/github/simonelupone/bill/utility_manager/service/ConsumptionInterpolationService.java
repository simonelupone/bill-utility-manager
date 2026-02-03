package com.github.simonelupone.bill.utility_manager.service;

import com.github.simonelupone.bill.utility_manager.domain.model.Reading;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TreeMap;

/**
 * Service responsible for calculating estimated energy consumption.
 * <p>
 * This service implements a Linear Interpolation algorithm to determine the
 * counter value
 * at specific dates (e.g., bill start and end dates) based on a discrete set of
 * readings.
 * </p>
 */
@Service
public class ConsumptionInterpolationService {

    private static final int SCALE = 2;
    private static final int INTERMEDIATE_SCALE = 10;

    /**
     * Calculates the consumption between two dates using linear interpolation.
     *
     * @param start    the start date of the period (inclusive)
     * @param end      the end date of the period (inclusive)
     * @param readings the list of available readings (must contain at least 2
     *                 readings covering the period)
     * @return the calculated consumption (difference between end and start values)
     * @throws IllegalArgumentException if start is after end, or if readings are
     *                                  insufficient/invalid
     */
    public BigDecimal calculateConsumption(LocalDate start, LocalDate end, List<Reading> readings) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        if (readings == null || readings.size() < 2) {
            throw new IllegalArgumentException("At least 2 readings are required for interpolation.");
        }

        TreeMap<LocalDate, Reading> timeSeries = new TreeMap<>();
        for (Reading r : readings) {
            timeSeries.put(r.date(), r);
        }

        BigDecimal startValue = interpolateValueAt(start, timeSeries);
        BigDecimal endValue = interpolateValueAt(end, timeSeries);

        return endValue.subtract(startValue).max(BigDecimal.ZERO);
    }

    /**
     * Interpolates the counter value at a specific target date.
     * <p>
     * Finds the closest readings before (floor) and after (ceiling) the target date
     * and applies the linear equation to estimate the value.
     * </p>
     *
     * @param targetDate the date for which the value is requested
     * @param series     the time series of readings ordered by date
     * @return the estimated counter value at the target date
     * @throws IllegalArgumentException if the target date is outside the range of
     *                                  available readings
     */
    private BigDecimal interpolateValueAt(LocalDate targetDate, TreeMap<LocalDate, Reading> series) {
        if (series.containsKey(targetDate)) {
            return BigDecimal.valueOf(series.get(targetDate).kWh());
        }

        LocalDate floorDate = series.floorKey(targetDate);
        LocalDate ceilingDate = series.ceilingKey(targetDate);

        if (floorDate == null || ceilingDate == null) {
            throw new IllegalArgumentException(
                    String.format("Cannot interpolate for date %s. Range available: [%s to %s]",
                            targetDate, series.firstKey(), series.lastKey()));
        }

        Reading floorReading = series.get(floorDate);
        Reading ceilingReading = series.get(ceilingDate);

        long daysTotal = ChronoUnit.DAYS.between(floorDate, ceilingDate);
        long daysFromFloor = ChronoUnit.DAYS.between(floorDate, targetDate);

        BigDecimal v1 = BigDecimal.valueOf(floorReading.kWh());
        BigDecimal v2 = BigDecimal.valueOf(ceilingReading.kWh());

        // Slope = (y2 - y1) / (x2 - x1)
        BigDecimal slope = v2.subtract(v1)
                .divide(BigDecimal.valueOf(daysTotal), INTERMEDIATE_SCALE, RoundingMode.HALF_UP);

        // y = y1 + (slope * daysFromFloor)
        BigDecimal increase = slope.multiply(BigDecimal.valueOf(daysFromFloor));

        return v1.add(increase).setScale(SCALE, RoundingMode.HALF_UP);
    }
}