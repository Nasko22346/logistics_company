package org.informatics.logistics_company.dto.price_location_tax;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceLocationTaxForm {
    private Long id;

    @NotNull(message = "Location tax is required")
    @Positive(message = "Location tax must be positive")
    private BigDecimal locationTax;

    @NotNull(message = "Location is required")
    private Long locationId;
}
