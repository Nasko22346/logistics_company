package org.informatics.logistics_company.dto.reports;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RevenueRow(
        Long parcelId,
        String trackingNumber,
        LocalDateTime sentDate,
        LocalDateTime receivedDate,
        String status,
        BigDecimal basePrice,
        BigDecimal weightTax,
        BigDecimal locationTax,
        BigDecimal total
) {}
