package com.github.simonelupone.bill.utility_manager.service;

import com.github.simonelupone.bill.utility_manager.domain.model.Bill;
import com.github.simonelupone.bill.utility_manager.domain.model.BillCharges;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Service responsible for calculating the cost breakdown between Tenant and
 * Owner.
 * <p>
 * Business Logic:
 * <ul>
 * <li><strong>Variable Costs:</strong> Proportional to consumption (Tenant kWh
 * / Total kWh).</li>
 * <li><strong>Fixed Costs:</strong> Split 50/50.</li>
 * <li><strong>Personal Costs:</strong> (TV Tax, Social Bonus) applied strictly
 * to the Owner (Apt 1).</li>
 * </ul>
 * </p>
 */
@Service
public class BillSplitterService {

        private static final int SCALE = 2;
        private static final int PERCENTAGE_SCALE = 6;

        /**
         * Result DTO containing the calculated amounts.
         * tenantRatio is a calculated percentage
         */
        public record SplitResult(
                        BigDecimal tenantTotal,
                        BigDecimal ownerTotal,
                        BigDecimal tenantKwh,
                        BigDecimal tenantRatio,
                        String calculationDetails) {
        }

        /**
         * Splits the bill costs based on the tenant's consumption.
         *
         * @param bill      the full electricity bill
         * @param tenantKwh the calculated/interpolated consumption for the tenant
         * @return a {@link SplitResult} with the breakdown
         */
        public SplitResult splitBill(Bill bill, BigDecimal tenantKwh) {
                BillCharges charges = bill.charges();
                BigDecimal totalKwh = bill.totalKwh();

                // 1. Calculate Tenant Ratio (Avoid division by zero)
                BigDecimal tenantRatio = BigDecimal.ZERO;
                if (totalKwh.compareTo(BigDecimal.ZERO) > 0) {
                        tenantRatio = tenantKwh.divide(totalKwh, PERCENTAGE_SCALE, RoundingMode.HALF_UP);
                }

                // 2. Calculate Variable Share (Energy + Excise/VAT)
                // Note: As per specs, Excise and VAT are treated as proportional.
                BigDecimal totalVariableCost = charges.energyVariable().amount()
                                .add(charges.exciseAndVat().amount());

                BigDecimal tenantVariableShare = totalVariableCost.multiply(tenantRatio);

                // 3. Calculate Fixed Share (Transport Fixed + Power Quota) -> 50% split
                BigDecimal totalFixedCost = charges.transportFixed().amount()
                                .add(charges.transportPowerQuota().amount());

                BigDecimal tenantFixedShare = totalFixedCost.divide(BigDecimal.valueOf(2), SCALE, RoundingMode.HALF_UP);

                // 4. Calculate Final Tenant Total
                BigDecimal tenantTotal = tenantVariableShare.add(tenantFixedShare).setScale(SCALE,
                                RoundingMode.HALF_UP);

                // 5. Calculate Owner Total (Bill Total - Tenant Total)
                // The owner absorbs everything else (TV Tax, remaining variable, remaining
                // fixed, Bonus)
                BigDecimal billTotal = bill.getTotalAmount();
                BigDecimal ownerTotal = billTotal.subtract(tenantTotal);

                String details = String.format(
                                "Tenant Consumption: %.2f kWh (%.2f%% of Total %.2f kWh). Variable Share: %s, Fixed Share: %s",
                                tenantKwh,
                                tenantRatio.multiply(BigDecimal.valueOf(100)).doubleValue(),
                                totalKwh,
                                tenantVariableShare.setScale(2, RoundingMode.HALF_UP),
                                tenantFixedShare);

                return new SplitResult(tenantTotal, ownerTotal, tenantKwh, tenantRatio, details);
        }
}