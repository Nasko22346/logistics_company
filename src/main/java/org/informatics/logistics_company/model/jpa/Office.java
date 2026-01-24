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
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "open_time_id")
    private OpenTime openTime;

    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;

}

