package org.informatics.logistics_company.model.jpa;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "office")
@Data
public class Office {
    @Id
    @Column(name = "office_id")
    private Long officeId;

    // To be removed according to the Location implementation
//    @Column(name = "office_address")
//    private String officeAddress;

    @Column(name = "office_phone")
    private String officePhone;

    @Column(name = "office_email")
    private String officeEmail;

    @ManyToOne
    private Company company;

    @ManyToOne
    private OpenTime openTime;

    // Todo implement One to One relation for Location
}

