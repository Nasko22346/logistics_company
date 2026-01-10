package org.informatics.logistics_company.repository;

import org.informatics.logistics_company.model.jpa.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Long> {
    @EntityGraph(attributePaths = "company")
    List<Office> findAll();
}
