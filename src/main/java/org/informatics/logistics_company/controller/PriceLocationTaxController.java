package org.informatics.logistics_company.controller;

import jakarta.validation.Valid;
import org.informatics.logistics_company.dto.price_location_tax.PriceLocationTaxRequest;
import org.informatics.logistics_company.dto.price_location_tax.PriceLocationTaxResponse;
import org.informatics.logistics_company.service.PriceLocationTaxService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/price-location-tax")
public class PriceLocationTaxController {

    private final PriceLocationTaxService service;

    public PriceLocationTaxController(PriceLocationTaxService service) {
        this.service = service;
    }

    @GetMapping("/load")
    public List<PriceLocationTaxResponse> load() {
        return service.loadData();
    }

    @PostMapping("/post")
    public ResponseEntity<PriceLocationTaxResponse> create(@Valid @RequestBody PriceLocationTaxRequest request) {
        PriceLocationTaxResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/api/v1/price-location-tax/" + created.id())).body(created);
    }

    @GetMapping("/{id}")
    public PriceLocationTaxResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public PriceLocationTaxResponse update(@PathVariable Long id, @Valid @RequestBody PriceLocationTaxRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
