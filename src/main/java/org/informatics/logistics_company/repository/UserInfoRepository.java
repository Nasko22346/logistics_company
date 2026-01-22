package org.informatics.logistics_company.repository;

import org.informatics.logistics_company.model.jpa.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> { }
