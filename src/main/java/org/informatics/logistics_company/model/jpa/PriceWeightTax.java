package org.informatics.logistics_company.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Table(name = "price_weight_tax")
@Entity
public class PriceWeightTax {

    @Id
    @Column(name = "price_weight_id")
    private Long id;

    @Column(name = "max_weight_amount")
    private Double maxWeightAmount;

    @Column(name = "price_weight_tax")
    private BigDecimal weightTax;
}
