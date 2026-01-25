package org.informatics.logistics_company.controller;

import org.informatics.logistics_company.service.ParcelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/reports")
public class ReportsPageController {

    private final ParcelService parcelService;

    public ReportsPageController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    @GetMapping("/revenue")
    public String revenue(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(defaultValue = "true") boolean excludeCancelled,
            Model model
    ) {
        LocalDate today = LocalDate.now();
        if (to == null) to = today;
        if (from == null) from = to.minusDays(30);

        model.addAttribute("report", parcelService.revenueReport(from, to, excludeCancelled));
        model.addAttribute("excludeCancelled", excludeCancelled);
        return "revenue-report";
    }
}
