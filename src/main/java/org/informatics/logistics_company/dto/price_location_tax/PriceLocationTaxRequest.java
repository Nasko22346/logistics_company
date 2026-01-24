package org.informatics.logistics_company.dto.price_location_tax;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import org.informatics.logistics_company.dto.templates.ObjectRequest;

import java.math.BigDecimal;

public record PriceLocationTaxRequest(
        @NotNull
        @DecimalMin(value = "0.00", inclusive = true)
        @Digits(integer = 10, fraction = 2)
        BigDecimal locationTax,


        @NotNull
        Long locationId
) implements ObjectRequest {}
