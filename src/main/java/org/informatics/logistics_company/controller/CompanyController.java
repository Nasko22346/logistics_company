package org.informatics.logistics_company.controller;

import org.informatics.logistics_company.service.CompanyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }


    //TODO: Implement controller methods - Get, Post, Put, Delete
}
