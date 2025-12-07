package org.informatics.logistics_company.dto.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public record CompanyRequest(

        @NotBlank @Size(max = 120)
        String companyName,

        @Size(max = 40)
        String phoneNumber,

        @Email @Size(max = 160)
        String email,

        @NotBlank @Size(max = 20)
        String companyEik,

        @Size(max = 255)
        String companyDescription
) {}
