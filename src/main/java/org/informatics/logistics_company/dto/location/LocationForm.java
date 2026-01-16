package org.informatics.logistics_company.dto.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationForm {
    private Long id;
    private String locationCountry;
    private String province;
    private String locationRegion;
    private String locationDescription;
}
