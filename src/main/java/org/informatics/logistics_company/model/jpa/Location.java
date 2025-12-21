package org.informatics.logistics_company.model.jpa;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "location")
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "location_country")
    private String locationCountry;

    @Column(name = "location_province")
    private String province;

    @Column(name = "location_region")
    private String locationRegion;

    @Column(name = "location_description")
    private String locationDescription;

}
