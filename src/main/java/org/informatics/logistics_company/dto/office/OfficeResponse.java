package org.informatics.logistics_company.dto.office;

import org.informatics.logistics_company.dto.templates.ObjectResponse;
import org.informatics.logistics_company.model.jpa.Location;

public record OfficeResponse (
        Long officeId,
        String officePhone,
        String officeEmail,
        Long companyId,
        String companyName,
        Location location// maybe, this should not fetch the entire location object, same fot the other. Only IDs (Long) need to be fetched.
) implements ObjectResponse { }
