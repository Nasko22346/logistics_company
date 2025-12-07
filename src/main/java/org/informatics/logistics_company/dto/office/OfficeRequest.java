package org.informatics.logistics_company.dto.office;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public record OfficeRequest (

        @NotBlank @Size(max = 40)
        String officePhone,

        @Email @Size(max = 120)
        String officeEmail
)
{ }
