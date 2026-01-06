package org.informatics.logistics_company.repository;

import org.informatics.logistics_company.model.jpa.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
}
