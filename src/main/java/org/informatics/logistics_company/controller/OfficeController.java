package org.informatics.logistics_company.controller;


import jakarta.validation.Valid;
import org.informatics.logistics_company.dto.office.OfficeRequest;
import org.informatics.logistics_company.dto.office.OfficeResponse;
import org.informatics.logistics_company.service.OfficeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/office")
public class OfficeController {
    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) { this.officeService = officeService; }

    @GetMapping("load")
    public List<OfficeResponse> loadOffices() {
        return officeService.loadData();
    }

    @PostMapping("/post")
    public ResponseEntity<OfficeResponse> create(@Valid @RequestBody OfficeRequest request) {
        OfficeResponse created = officeService.create(request);
        //Location header -> /api/v1/office/{id}
        return ResponseEntity
                .created(URI.create("/api/v1/office/" + created.officeId()))
                .body(created);
    }

    @GetMapping("/{id}")
    public OfficeResponse getById(@PathVariable Long id) {
        System.out.println("Get for id " + id );
        return officeService.getById(id);
    }

    @PutMapping("/{id}")
    public OfficeResponse update(@PathVariable Long id,
                                 @Valid @RequestBody OfficeRequest request){
        return officeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        officeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
