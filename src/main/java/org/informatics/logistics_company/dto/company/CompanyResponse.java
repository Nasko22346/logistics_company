package org.informatics.logistics_company.dto.company;

import org.informatics.logistics_company.dto.templates.ObjectResponse;

import java.util.List;

public record CompanyResponse(
        Long companyId,
        String companyName,
        String phoneNumber,
        String email,
        String companyEik,
        String companyDescription,
        List<Long> officeIds
) implements ObjectResponse {}
