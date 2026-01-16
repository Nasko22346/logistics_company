package org.informatics.logistics_company.dto.price_location_tax;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceLocationTaxForm {
    private Long id;
    private BigDecimal locationTax;
    private Long locationId;
}
