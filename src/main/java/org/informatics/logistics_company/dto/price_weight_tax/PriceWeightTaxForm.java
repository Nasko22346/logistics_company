package org.informatics.logistics_company.dto.price_weight_tax;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceWeightTaxForm {

    private Long id;

    @NotNull(message = "Max weight is required.")
    @Positive(message = "Max weight must be positive.")
    private Double maxWeightAmount;

    @NotNull(message = "Weight tax is required.")
    @DecimalMin(value = "0.00", inclusive = true, message = "Weight tax must be >= 0.")
    @Digits(integer = 12, fraction = 2, message = "Weight tax must have up to 12 digits and 2 decimals.")
    private BigDecimal weightTax;
}
