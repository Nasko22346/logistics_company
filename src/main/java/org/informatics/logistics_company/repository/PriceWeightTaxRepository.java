package org.informatics.logistics_company.repository;

import org.informatics.logistics_company.model.jpa.PriceWeightTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceWeightTaxRepository extends JpaRepository<PriceWeightTax, Long> {
}
