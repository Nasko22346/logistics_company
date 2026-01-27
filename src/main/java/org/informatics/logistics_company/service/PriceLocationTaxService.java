package org.informatics.logistics_company.service;

import org.informatics.logistics_company.dto.price_location_tax.PriceLocationTaxRequest;
import org.informatics.logistics_company.dto.price_location_tax.PriceLocationTaxResponse;
import org.informatics.logistics_company.model.jpa.Location;
import org.informatics.logistics_company.model.jpa.PriceLocationTax;
import org.informatics.logistics_company.repository.LocationRepository;
import org.informatics.logistics_company.repository.PriceLocationTaxRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceLocationTaxService {

    private final PriceLocationTaxRepository repo;
    private final LocationRepository locationRepo;

    public PriceLocationTaxService(PriceLocationTaxRepository repo, LocationRepository locationRepo) {
        this.repo = repo;
        this.locationRepo = locationRepo;
    }

    public List<PriceLocationTaxResponse> loadData() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    public PriceLocationTaxResponse create(PriceLocationTaxRequest request) {
        Location location = locationRepo.findById(request.locationId())
                .orElseThrow(() -> new RuntimeException("Location with id " + request.locationId() + " not found"));

        PriceLocationTax entity = new PriceLocationTax();
        entity.setLocationTax(request.locationTax());
        entity.setLocation(location);

        return toResponse(repo.save(entity));
    }

    public PriceLocationTaxResponse getById(Long id) {
        PriceLocationTax entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("PriceLocationTax with id " + id + " not found"));
        return toResponse(entity);
    }

    public PriceLocationTaxResponse update(Long id, PriceLocationTaxRequest request) {
        PriceLocationTax entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("PriceLocationTax with id " + id + " not found"));

        Location location = locationRepo.findById(request.locationId())
                .orElseThrow(() -> new RuntimeException("Location with id " + request.locationId() + " not found"));

        entity.setLocationTax(request.locationTax());
        entity.setLocation(location);

        return toResponse(repo.save(entity));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("PriceLocationTax with id " + id + " not found");
        }
        repo.deleteById(id);
    }

    private PriceLocationTaxResponse toResponse(PriceLocationTax entity) {
        Long locationId = entity.getLocation() != null ? getLocationId(entity.getLocation()) : null;
        String label = formatLocationLabel(entity.getLocation());

        return new PriceLocationTaxResponse(
                entity.getId(),
                entity.getLocationTax(),
                locationId,
                label
        );
    }

    private String formatLocationLabel(Location location) {
        if (location == null) return null;

        StringBuilder sb = new StringBuilder();
        if (location.getLocationCountry() != null && !location.getLocationCountry().isBlank()) {
            sb.append(location.getLocationCountry());
        }
        if (location.getProvince() != null && !location.getProvince().isBlank()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(location.getProvince());
        }
        if (location.getLocationRegion() != null && !location.getLocationRegion().isBlank()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(location.getLocationRegion());
        }
        return !sb.isEmpty() ? sb.toString() : "Location #" + location.getLocationId();
    }

    /**
     * ⚠️ ВАЖНО: ако Location id полето ти НЕ се казва getLocationId(), смени го тук.
     */
    private Long getLocationId(Location location) {
        // най-често е location.getLocationId() или location.getId()
        try {
            return (Long) Location.class.getMethod("getLocationId").invoke(location);
        } catch (Exception ignored) {}

        try {
            return (Long) Location.class.getMethod("getId").invoke(location);
        } catch (Exception e) {
            throw new RuntimeException("Cannot resolve Location id getter. Rename getLocationId/getId in PriceLocationTaxService.");
        }
    }

    public List<Location> getAllLocations() {
        return locationRepo.findAll();
    }

    public List<PriceLocationTaxResponse> search(String q) {
        var all = loadData();

        if (q == null || q.isBlank()) {
            return all;
        }

        String qq = q.trim().toLowerCase();

        return all.stream().filter(r -> {
            String idStr = r.id() != null ? String.valueOf(r.id()) : "";
            String taxStr = r.locationTax() != null ? r.locationTax().toPlainString() : "";
            String locIdStr = r.locationId() != null ? String.valueOf(r.locationId()) : "";
            String locLabel = r.locationLabel() != null ? r.locationLabel().toLowerCase() : "";

            return idStr.contains(qq) || taxStr.contains(qq) || locIdStr.contains(qq) || locLabel.contains(qq);
        }).toList();
    }
}
