package org.informatics.logistics_company.dto.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.informatics.logistics_company.dto.templates.ObjectRequest;

public record LocationRequest(
        @NotBlank @Size(max = 80)
        String locationCountry,

        @Size(max = 80)
        String province,

        @Size(max = 120)
        String locationRegion,

        @Size(max = 255)
        String locationDescription
) implements ObjectRequest { }
