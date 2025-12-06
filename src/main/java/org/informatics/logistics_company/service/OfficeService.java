package org.informatics.logistics_company.service;

import org.informatics.logistics_company.rr.office.OfficeRequest;
import org.informatics.logistics_company.rr.office.OfficeResponse;
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
                office.getCompany(),
                office.getOpenTime(),
                office.getLocation()
        );
    }
}
