package org.informatics.logistics_company.repository;

import org.informatics.logistics_company.model.jpa.Company;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Override
    @EntityGraph(attributePaths = {"offices"})
    List<Company> findAll();

    @Override
    @EntityGraph(attributePaths = {"offices"})
    Optional<Company> findById(Long id);

    List<Company> findByCompanyNameContainingIgnoreCaseOrCompanyEikContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
            String name, String eik, String email, String phone
    );
}
