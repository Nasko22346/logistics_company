package org.informatics.logistics_company.repository;

import org.informatics.logistics_company.model.jpa.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
}
