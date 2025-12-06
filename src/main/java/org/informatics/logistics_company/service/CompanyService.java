package org.informatics.logistics_company.service;

import org.informatics.logistics_company.rr.company.CompanyRequest;
import org.informatics.logistics_company.rr.company.CompanyResponse;
import org.informatics.logistics_company.model.jpa.Company;
import org.informatics.logistics_company.model.jpa.Office;
import org.informatics.logistics_company.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {


    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyResponse create(CompanyRequest request) {
        Company entity = new Company();
        entity.setCompanyName(request.companyName());
        entity.setPhoneNumber(request.phoneNumber());
        entity.setEmail(request.email());
        entity.setCompanyEik(request.companyEik());
        entity.setCompanyDescription(request.companyDescription());

        Company saved = companyRepository.save(entity);
        return toResponse(saved);
    }

    public CompanyResponse getById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company with id " + id + " not found"));
        return toResponse(company);
    }

    public List<CompanyResponse> getAll() {
        return companyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CompanyResponse update(Long id, CompanyRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company with id " + id + " not found"));

        company.setCompanyName(request.companyName());
        company.setPhoneNumber(request.phoneNumber());
        company.setEmail(request.email());
        company.setCompanyEik(request.companyEik());
        company.setCompanyDescription(request.companyDescription());

        Company updated = companyRepository.save(company);
        return toResponse(updated);
    }

    public void delete(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new RuntimeException("Company with id " + id + " not found");
        }
        companyRepository.deleteById(id);
    }

    private CompanyResponse toResponse(Company company) {
        List<Long> officeIds = company.getOffices() == null
                ? List.of()
                : company.getOffices().stream()
                .map(Office::getOfficeId)
                .toList();

        return new CompanyResponse(
                company.getCompanyId(),
                company.getCompanyName(),
                company.getPhoneNumber(),
                company.getEmail(),
                company.getCompanyEik(),
                company.getCompanyDescription(),
                officeIds
        );
    }

    //TODO: Consult further for more business logic
}
