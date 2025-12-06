package org.informatics.logistics_company.controller;

import org.informatics.logistics_company.service.CompanyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/load")
    public List<?> loadCompanies() {
        return new ArrayList<>();
    }

    //TODO: Implement controller methods - Get, Post, Put, Delete
}
