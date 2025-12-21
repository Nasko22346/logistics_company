package org.informatics.logistics_company.model.jpa;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "office")
@Data
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "office_id")
    private Long officeId;

    @Column(name = "office_phone")
    private String officePhone;

    @Column(name = "office_email")
    private String officeEmail;

    @ManyToOne
    private Company company;

    @ManyToOne
    private OpenTime openTime;

    @OneToOne
    private Location location;
}

