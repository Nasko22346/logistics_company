package org.informatics.logistics_company.repository;

import org.informatics.logistics_company.model.jpa.Staff;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Override
    @EntityGraph(attributePaths = {"office", "staffUserDetails", "staffUserDetails.loginDetails"})
    List<Staff> findAll();

    @EntityGraph(attributePaths = {"office", "staffUserDetails", "staffUserDetails.loginDetails"})
    @Query("""
        select distinct s from Staff s
        join s.staffUserDetails ui
        join ui.loginDetails li
        where lower(li.email) like lower(concat('%', :q, '%'))
           or lower(ui.firstName) like lower(concat('%', :q, '%'))
           or lower(ui.middleName) like lower(concat('%', :q, '%'))
           or lower(ui.lastName) like lower(concat('%', :q, '%'))
           or lower(ui.phoneNumber) like lower(concat('%', :q, '%'))
    """)
    List<Staff> search(@Param("q") String q);

    boolean existsByStaffUserDetails_Id(Long userId);
}
