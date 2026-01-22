package org.informatics.logistics_company.repository;

import org.informatics.logistics_company.model.jpa.Staff;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Override
    @EntityGraph(attributePaths = {"office", "staffUserInfo", "staffUserInfo.loginInfo"})
    List<Staff> findAll();

    @EntityGraph(attributePaths = {"office", "staffUserInfo", "staffUserInfo.loginInfo"})
    @Query("""
        select distinct s from Staff s
        join s.staffUserInfo ui
        join ui.loginInfo li
        where lower(li.email) like lower(concat('%', :q, '%'))
           or lower(ui.firstName) like lower(concat('%', :q, '%'))
           or lower(ui.middleName) like lower(concat('%', :q, '%'))
           or lower(ui.lastName) like lower(concat('%', :q, '%'))
           or lower(ui.phoneNumber) like lower(concat('%', :q, '%'))
    """)
    List<Staff> search(@Param("q") String q);
}
