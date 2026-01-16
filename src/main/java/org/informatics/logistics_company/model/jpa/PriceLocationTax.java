package org.informatics.logistics_company.model.jpa;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Table(name = "price_location_tax")
@Entity
public class PriceLocationTax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_location_tax_id")
    private Long id;

    @Column(name = "price_location_tax", nullable = false)
    private BigDecimal locationTax;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false, unique = true)
    private Location location;
}
