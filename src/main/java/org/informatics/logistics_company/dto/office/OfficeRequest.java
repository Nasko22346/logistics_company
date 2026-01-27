package org.informatics.logistics_company.dto.office;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import org.informatics.logistics_company.dto.templates.ObjectRequest;

public record OfficeRequest (

        @NotBlank @Size(max = 40)
        String officePhone,

        @Email @Size(max = 120)
        String officeEmail,

        Long companyId,
        Long locationId
) implements ObjectRequest { }
