package org.informatics.logistics_company.service;

import org.informatics.logistics_company.dto.location.LocationRequest;
import org.informatics.logistics_company.dto.location.LocationResponse;
import org.informatics.logistics_company.model.jpa.Location;
import org.informatics.logistics_company.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationResponse> loadData() {
        return locationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<LocationResponse> search(String q) {
        if (q == null || q.isBlank()) return loadData();

        return locationRepository
                .findByLocationCountryContainingIgnoreCaseOrProvinceContainingIgnoreCaseOrLocationRegionContainingIgnoreCaseOrLocationDescriptionContainingIgnoreCase(
                        q, q, q, q
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public LocationResponse create(LocationRequest request) {
        Location entity = new Location();
        entity.setLocationCountry(request.locationCountry());
        entity.setProvince(request.province());
        entity.setLocationRegion(request.locationRegion());
        entity.setLocationDescription(request.locationDescription());

        Location saved = locationRepository.save(entity);
        return toResponse(saved);
    }

    public LocationResponse getById(Long id) {
        Location loc = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location with id " + id + " not found"));
        return toResponse(loc);
    }

    public LocationResponse update(Long id, LocationRequest request) {
        Location loc = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location with id " + id + " not found"));

        loc.setLocationCountry(request.locationCountry());
        loc.setProvince(request.province());
        loc.setLocationRegion(request.locationRegion());
        loc.setLocationDescription(request.locationDescription());

        Location updated = locationRepository.save(loc);
        return toResponse(updated);
    }

    public void delete(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new RuntimeException("Location with id " + id + " not found");
        }
        locationRepository.deleteById(id);
    }

    private LocationResponse toResponse(Location l) {
        return new LocationResponse(
                l.getLocationId(),
                l.getLocationCountry(),
                l.getProvince(),
                l.getLocationRegion(),
                l.getLocationDescription()
        );
    }
}
