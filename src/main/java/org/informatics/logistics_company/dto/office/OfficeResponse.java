package org.informatics.logistics_company.dto.office;

import org.informatics.logistics_company.dto.templates.ObjectResponse;
import org.informatics.logistics_company.model.jpa.Location;
import org.informatics.logistics_company.model.jpa.OpenTime;

public record OfficeResponse (
        Long officeId,
        String officePhone,
        String officeEmail,
        Long companyId,
        String companyName,
        OpenTime openTime,
        Location location// maybe, this should not fetch the entire location object, same fot the other. Only IDs (Long) need to be fetched.
) implements ObjectResponse { }
