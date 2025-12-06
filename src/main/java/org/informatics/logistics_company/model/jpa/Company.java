package org.informatics.logistics_company.model.jpa;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "companies")
@Data
public class Company {

    @Id
    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "company_eik")
    private String companyEik;

    @Column(name = "company_description")
    private String companyDescription;

    @OneToMany(mappedBy = "company")
    private List<Office> offices;
}
