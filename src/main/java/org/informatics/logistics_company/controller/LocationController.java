package org.informatics.logistics_company.controller;

import jakarta.validation.Valid;
import org.informatics.logistics_company.dto.location.LocationRequest;
import org.informatics.logistics_company.dto.location.LocationResponse;
import org.informatics.logistics_company.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/load")
    public List<LocationResponse> load() {
        return locationService.loadData();
    }

    @PostMapping("/post")
    public ResponseEntity<LocationResponse> create(@Valid @RequestBody LocationRequest request) {
        LocationResponse created = locationService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/locations/" + created.locationId()))
                .body(created);
    }

    @GetMapping("/{id}")
    public LocationResponse getById(@PathVariable Long id) {
        return locationService.getById(id);
    }

    @PutMapping("/{id}")
    public LocationResponse update(@PathVariable Long id, @Valid @RequestBody LocationRequest request) {
        return locationService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        locationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
