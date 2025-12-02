package org.informatics.logistics_company.controller;

import jakarta.validation.Valid;
import org.informatics.logistics_company.dto.company.CompanyRequest;
import org.informatics.logistics_company.dto.company.CompanyResponse;
import org.informatics.logistics_company.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    //TODO: Revisit implementation
    @GetMapping("/load")
    public List<?> loadCompanies() {
        return new ArrayList<>();
    }

    @PostMapping("/post")
    public ResponseEntity<CompanyResponse> create(@Valid @RequestBody CompanyRequest request) {
        CompanyResponse created = companyService.create(request);
        // Location header → /api/v1/companies/{id}
        return ResponseEntity
                .created(URI.create("/api/v1/companies/" + created.companyId()))
                .body(created);
    }

    @GetMapping("/{id}")
    public CompanyResponse getById(@PathVariable Long id) {
        return companyService.getById(id);
    }

    @PutMapping("/{id}")
    public CompanyResponse update(@PathVariable Long id,
                                  @Valid @RequestBody CompanyRequest request) {
        return companyService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
