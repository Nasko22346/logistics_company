package org.informatics.logistics_company.service;

import org.informatics.logistics_company.dto.office.OfficeRequest;
import org.informatics.logistics_company.dto.office.OfficeResponse;
import org.informatics.logistics_company.model.jpa.Office;
import org.informatics.logistics_company.repository.OfficeRepository;
import org.springframework.stereotype.Service;

@Service
public class OfficeService {

    private final OfficeRepository officeRepository;

    public OfficeService(OfficeRepository officeRepository){
        this.officeRepository = officeRepository;
    }

    public OfficeResponse create(OfficeRequest request) {
        Office entity = new Office();
        entity.setOfficePhone(request.officePhone());
        entity.setOfficeEmail(request.officeEmail());

        // TODO: Determine during the creation of the company how to add relations to location, company & opentime.

        Office saved = officeRepository.save(entity);
        return toResponse(saved);
    }

    private OfficeResponse toResponse(Office office) {
        // TODO: See here how to fetch and return location, company & opentime

        return new OfficeResponse(
                office.getOfficeId(),
                office.getOfficePhone(),
                office.getOfficeEmail(),
                office.getCompany(),
                office.getOpenTime(),
                office.getLocation()
        );
    }
}
