package org.informatics.logistics_company.repository;

import org.informatics.logistics_company.model.jpa.UserDetails;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    @EntityGraph(attributePaths = {"loginDetails"})
    @Query("""
        select u from UserDetails u
        where not exists (
            select 1 from Staff s
            where s.staffUserDetails = u
        )
        order by u.id desc
    """)
    List<UserDetails> findAllClients();

    @EntityGraph(attributePaths = {"loginDetails"})
    @Query("""
        select u from UserDetails u
        where not exists (
            select 1 from Staff s
            where s.staffUserDetails = u
        )
        and (
            :q is null or :q = ''
            or lower(concat(coalesce(u.firstName,''),' ',coalesce(u.middleName,''),' ',coalesce(u.lastName,'')))
                like lower(concat('%', :q, '%'))
            or lower(coalesce(u.phoneNumber,'')) like lower(concat('%', :q, '%'))
            or lower(coalesce(u.loginDetails.email,'')) like lower(concat('%', :q, '%'))
        )
        order by u.id desc
    """)
    List<UserDetails> searchClients(@Param("q") String q);

}
