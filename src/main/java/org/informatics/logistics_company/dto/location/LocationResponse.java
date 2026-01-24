package org.informatics.logistics_company.dto.location;

import org.informatics.logistics_company.dto.templates.ObjectResponse;

public record LocationResponse(
        Long locationId,
        String locationCountry,
        String province,
        String locationRegion,
        String locationDescription
) implements ObjectResponse { }
