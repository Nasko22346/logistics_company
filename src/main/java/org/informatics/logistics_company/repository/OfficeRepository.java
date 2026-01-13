package org.informatics.logistics_company.repository;

import org.informatics.logistics_company.model.jpa.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Long> {

    @Override
    @EntityGraph(attributePaths = {"company", "openTime", "location"})
    List<Office> findAll();

    boolean existsByLocation_LocationId(Long locationId);

    boolean existsByLocation_LocationIdAndOfficeIdNot(Long locationId, Long officeId);

    @EntityGraph(attributePaths = {"company", "openTime", "location"})
    List<Office> findAllByCompany_CompanyId(Long companyId);

    @EntityGraph(attributePaths = {"company", "openTime", "location"})
    List<Office> findByOfficePhoneContainingIgnoreCaseOrOfficeEmailContainingIgnoreCaseOrCompany_CompanyNameContainingIgnoreCase(
            String phone, String email, String companyName
    );

    @EntityGraph(attributePaths = {"company", "openTime", "location"})
    List<Office> findByCompany_CompanyIdAndOfficePhoneContainingIgnoreCaseOrCompany_CompanyIdAndOfficeEmailContainingIgnoreCaseOrCompany_CompanyIdAndCompany_CompanyNameContainingIgnoreCase(
            Long companyId1, String phone,
            Long companyId2, String email,
            Long companyId3, String companyName
    );
}
