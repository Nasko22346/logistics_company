package org.informatics.logistics_company.repository;

import org.informatics.logistics_company.model.jpa.PriceLocationTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceLocationTaxRepository extends JpaRepository<PriceLocationTax, Long> {
    java.util.Optional<PriceLocationTax> findByLocation_LocationId(Long locationId);
}
