package org.informatics.logistics_company.repository;

import org.informatics.logistics_company.model.jpa.LoginDetails;
import org.informatics.logistics_company.model.jpa.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginDetailsRepository extends JpaRepository<LoginDetails, Long> {
    Optional<LoginDetails> findByEmail(String email);
}
