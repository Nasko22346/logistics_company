package org.informatics.logistics_company.controller;

import org.informatics.logistics_company.dto.staff.StaffResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {
    @GetMapping("/{id}")
    public StaffResponse getStaff(@PathVariable Long id) {
        return null;
    }
}
