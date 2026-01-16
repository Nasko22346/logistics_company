package org.informatics.logistics_company.dto.price_location_tax;

import org.informatics.logistics_company.dto.templates.ObjectResponse;

import java.math.BigDecimal;

public record PriceLocationTaxResponse(
        Long id,
        BigDecimal locationTax,
        Long locationId,
        String locationLabel
) implements ObjectResponse {}
