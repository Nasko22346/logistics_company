package org.informatics.logistics_company.dto.reports;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RevenueReport(
        LocalDate fromDate,
        LocalDate toDate,
        long parcelsCount,
        BigDecimal baseSum,
        BigDecimal weightTaxSum,
        BigDecimal locationTaxSum,
        BigDecimal totalRevenue,
        List<RevenueRow> rows
) {}
