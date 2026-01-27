package org.informatics.logistics_company.dto.parcel;

import java.math.BigDecimal;

public record PriceCalculationResponse(
    BigDecimal weightTax,
    BigDecimal locationTax,
    BigDecimal totalPrice
) {}
