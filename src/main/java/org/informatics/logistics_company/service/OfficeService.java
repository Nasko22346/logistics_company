package org.informatics.logistics_company.service;

import org.informatics.logistics_company.dto.office.OfficeRequest;
import org.informatics.logistics_company.dto.office.OfficeResponse;
import org.informatics.logistics_company.model.jpa.Office;
import org.informatics.logistics_company.repository.LocationRepository;
import org.informatics.logistics_company.repository.OfficeRepository;
import org.informatics.logistics_company.repository.CompanyRepository;
import org.informatics.logistics_company.repository.OpenTimeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.informatics.logistics_company.model.jpa.Company;


@Service
public class OfficeService {

    private final OfficeRepository officeRepository;
    private final CompanyRepository companyRepository;
    private final OpenTimeRepository openTimeRepository;
    private final LocationRepository locationRepository;

    public OfficeService(OfficeRepository officeRepository,
                         CompanyRepository companyRepository,
                         OpenTimeRepository openTimeRepository,
                         LocationRepository locationRepository) {
        this.officeRepository = officeRepository;
        this.companyRepository = companyRepository;
        this.openTimeRepository = openTimeRepository;
        this.locationRepository = locationRepository;
    }

    public List<OfficeResponse> loadData() {
        // TODO : MOVE commented code to a helper package or something similar.
//        if (officeRepository.count() == 0) {
//            Company companyId = companyRepository.findAll()
//                    .stream()
//                    .findFirst()
//                    .orElseThrow(() -> new RuntimeException("No companies found to attach offices to"));
//
//            Office o1 = new Office();
//            o1.setOfficePhone("0888123456");
//            o1.setOfficeEmail("sofia.office@test.com");
//            o1.setCompany(companyId);
//
//            Office o2 = new Office();
//            o2.setOfficePhone("0888765432");
//            o2.setOfficeEmail("varna.office@test.com");
//            o2.setCompany(companyId);
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

        // company - задължително (nullable=false)
        if (request.companyId() == null) throw new RuntimeException("companyId is required");
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new RuntimeException("Company with id " + request.companyId() + " not found"));
        entity.setCompany(company);

        // openTime - optional
        if (request.openTimeId() != null) {
            var ot = openTimeRepository.findById(request.openTimeId())
                    .orElseThrow(() -> new RuntimeException("OpenTime with id " + request.openTimeId() + " not found"));
            entity.setOpenTime(ot);
        } else {
            entity.setOpenTime(null);
        }

        // location - optional
        if (request.locationId() != null) {
            var loc = locationRepository.findById(request.locationId())
                    .orElseThrow(() -> new RuntimeException("Location with id " + request.locationId() + " not found"));
            entity.setLocation(loc);
        } else {
            entity.setLocation(null);
        }

        return toResponse(officeRepository.save(entity));
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

        // company
        if (request.companyId() == null) throw new RuntimeException("companyId is required");
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new RuntimeException("Company with id " + request.companyId() + " not found"));
        office.setCompany(company);

        // openTime optional
        if (request.openTimeId() != null) {
            var ot = openTimeRepository.findById(request.openTimeId())
                    .orElseThrow(() -> new RuntimeException("OpenTime with id " + request.openTimeId() + " not found"));
            office.setOpenTime(ot);
        } else {
            office.setOpenTime(null);
        }

        // location optional
        if (request.locationId() != null) {
            var loc = locationRepository.findById(request.locationId())
                    .orElseThrow(() -> new RuntimeException("Location with id " + request.locationId() + " not found"));
            office.setLocation(loc);
        } else {
            office.setLocation(null);
        }

        return toResponse(officeRepository.save(office));
    }



    public void delete(Long id) {
        if (!officeRepository.existsById(id)) {
            throw new RuntimeException("Office with id " + id + "not found");
        }
        officeRepository.deleteById(id);
    }

    private OfficeResponse toResponse(Office office) {
        Long companyId = office.getCompany() != null ? office.getCompany().getCompanyId() : null;
        String companyName = office.getCompany() != null ? office.getCompany().getCompanyName() : null;

        return new OfficeResponse(
                office.getOfficeId(),
                office.getOfficePhone(),
                office.getOfficeEmail(),
                companyId,
                companyName,
                office.getOpenTime(),
                office.getLocation()
        );
    }

    public OfficeResponse toResponsePublic(Office office) {
        Long companyId = office.getCompany() != null ? office.getCompany().getCompanyId() : null;
        String companyName = office.getCompany() != null ? office.getCompany().getCompanyName() : null;

        return new OfficeResponse(
                office.getOfficeId(),
                office.getOfficePhone(),
                office.getOfficeEmail(),
                companyId,
                companyName,
                office.getOpenTime(),
                office.getLocation()
        );
    }

    public List<OfficeResponse> search(String q, Long companyId) {
        if ((q == null || q.isBlank()) && companyId == null) {
            return loadData();
        }

        if (q == null) q = "";
        q = q.trim();

        List<Office> offices;

        if (companyId != null) {
            if (q.isBlank()) {
                offices = officeRepository.findAllByCompany_CompanyId(companyId);
            } else {
                offices = officeRepository
                        .findByCompany_CompanyIdAndOfficePhoneContainingIgnoreCaseOrCompany_CompanyIdAndOfficeEmailContainingIgnoreCaseOrCompany_CompanyIdAndCompany_CompanyNameContainingIgnoreCase(
                                companyId, q,
                                companyId, q,
                                companyId, q
                        );
            }
        } else {
            if (q.isBlank()) {
                offices = officeRepository.findAll();
            } else {
                offices = officeRepository
                        .findByOfficePhoneContainingIgnoreCaseOrOfficeEmailContainingIgnoreCaseOrCompany_CompanyNameContainingIgnoreCase(q, q, q);
            }
        }

        return offices.stream().map(this::toResponsePublic).toList();
    }
}
