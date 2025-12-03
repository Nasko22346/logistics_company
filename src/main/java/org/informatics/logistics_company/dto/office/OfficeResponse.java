package org.informatics.logistics_company.dto.office;

import org.informatics.logistics_company.model.jpa.Company;
import org.informatics.logistics_company.model.jpa.Location;
import org.informatics.logistics_company.model.jpa.OpenTime;

public record OfficeResponse (
         Long officeId,
         String officePhone,
         String officeEmail,
         Company company,
         OpenTime openTime,
         Location location
){ }
