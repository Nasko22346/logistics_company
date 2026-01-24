package org.informatics.logistics_company.repository;

import org.informatics.logistics_company.model.jpa.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByLocationCountryContainingIgnoreCaseOrProvinceContainingIgnoreCaseOrLocationRegionContainingIgnoreCaseOrLocationDescriptionContainingIgnoreCase(
            String country, String province, String region, String description
    );
}
