package org.informatics.logistics_company.service;

import org.informatics.logistics_company.repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {


    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    //TODO: Implement service methods for business logic
}
