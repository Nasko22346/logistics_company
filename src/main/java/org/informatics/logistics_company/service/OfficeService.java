package org.informatics.logistics_company.service;

import org.informatics.logistics_company.dto.office.OfficeRequest;
import org.informatics.logistics_company.dto.office.OfficeResponse;
import org.informatics.logistics_company.model.jpa.Office;
import org.informatics.logistics_company.repository.OfficeRepository;
import org.informatics.logistics_company.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OfficeService {

    private final OfficeRepository officeRepository;
    private final CompanyRepository companyRepository;

    public OfficeService(OfficeRepository officeRepository, CompanyRepository companyRepository){
        this.officeRepository = officeRepository;
        this.companyRepository = companyRepository;
    }

    public List<OfficeResponse> loadData() {
        // TODO : MOVE commented code to a helper package or something similar.
//        if (officeRepository.count() == 0) {
//            Company company = companyRepository.findAll()
//                    .stream()
//                    .findFirst()
//                    .orElseThrow(() -> new RuntimeException("No companies found to attach offices to"));
//
//            Office o1 = new Office();
//            o1.setOfficePhone("0888123456");
//            o1.setOfficeEmail("sofia.office@test.com");
//            o1.setCompany(company);
//
//            Office o2 = new Office();
//            o2.setOfficePhone("0888765432");
//            o2.setOfficeEmail("varna.office@test.com");
//            o2.setCompany(company);
//
//            officeRepository.saveAll(List.of(o1, o2));
//        }

        return officeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public OfficeResponse create(OfficeRequest request) {
        Office entity = new Office();
        entity.setOfficePhone(request.officePhone());
        entity.setOfficeEmail(request.officeEmail());

        // TODO: Determine during the creation of the company how to add relations to location, company & opentime.

        Office saved = officeRepository.save(entity);
        return toResponse(saved);
    }

    public OfficeResponse getById(Long id) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Office with id " + id + " not found"));
        return toResponse(office);
    }

    public OfficeResponse update(Long id, OfficeRequest request) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Office with id " + id + " not found"));

        office.setOfficePhone(request.officePhone());
        office.setOfficeEmail(request.officeEmail());

        Office updated = officeRepository.save(office);
        return toResponse(updated);
    }

    public void delete(Long id) {
        if (!officeRepository.existsById(id)) {
            throw new RuntimeException("Office with id " + id + "not found");
        }
        officeRepository.deleteById(id);
    }

    private OfficeResponse toResponse(Office office) {
        // TODO: See here how to fetch and return location, company & opentime

        return new OfficeResponse(
                office.getOfficeId(),
                office.getOfficePhone(),
                office.getOfficeEmail(),
                office.getCompany() != null ? office.getCompany().getCompanyId() : null,
                office.getOpenTime(),
                office.getLocation()
        );
    }
}
