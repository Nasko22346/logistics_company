package org.informatics.logistics_company.dto.company;

import java.util.List;

public record CompanyResponse(
        Long companyId,
        String companyName,
        String phoneNumber,
        String email,
        String companyEik,
        String companyDescription,
        List<Long> officeIds
) {}
