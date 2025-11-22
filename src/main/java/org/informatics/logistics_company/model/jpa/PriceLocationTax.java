package org.informatics.logistics_company.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Table(name = "price_location_tax")
@Entity
public class PriceLocationTax {

    @Id
    @Column(name = "price_location_tax_id")
    private Long id;

    @Column(name = "price_location_tax")
    private BigDecimal locationTax;
}
