package org.informatics.logistics_company.repository;

import org.informatics.logistics_company.model.jpa.LoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {
    boolean existsByEmailIgnoreCase(String email);
}
