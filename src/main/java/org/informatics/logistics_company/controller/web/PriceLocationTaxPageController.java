package org.informatics.logistics_company.controller.web;

import org.informatics.logistics_company.dto.price_location_tax.PriceLocationTaxForm;
import org.informatics.logistics_company.dto.price_location_tax.PriceLocationTaxRequest;
import org.informatics.logistics_company.repository.LocationRepository;
import org.informatics.logistics_company.service.PriceLocationTaxService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class PriceLocationTaxPageController {

    private final PriceLocationTaxService service;
    private final LocationRepository locationRepository;

    public PriceLocationTaxPageController(PriceLocationTaxService service, LocationRepository locationRepository) {
        this.service = service;
        this.locationRepository = locationRepository;
    }

    @GetMapping("/price-location-taxes")
    public String list(@RequestParam(required = false) String q, Model model) {
        var all = service.loadData();

        var filtered = all.stream().filter(r -> {
            if (q == null || q.isBlank()) return true;
            String qq = q.trim().toLowerCase();

            String idStr = r.id() != null ? String.valueOf(r.id()) : "";
            String taxStr = r.locationTax() != null ? r.locationTax().toPlainString() : "";
            String locIdStr = r.locationId() != null ? String.valueOf(r.locationId()) : "";
            String locLabel = r.locationLabel() != null ? r.locationLabel().toLowerCase() : "";

            return idStr.contains(qq) || taxStr.contains(qq) || locIdStr.contains(qq) || locLabel.contains(qq);
        }).toList();

        model.addAttribute("items", filtered);
        model.addAttribute("q", q);
        return "price-location-taxes";
    }

    @GetMapping("/price-location-taxes/new")
    public String createForm(Model model) {
        model.addAttribute("form", new PriceLocationTaxForm());
        model.addAttribute("locations", locationRepository.findAll());
        return "price-location-tax-form";
    }

    @PostMapping("/price-location-taxes")
    public String create(@ModelAttribute("form") PriceLocationTaxForm form) {
        service.create(new PriceLocationTaxRequest(form.getLocationTax(), form.getLocationId()));
        return "redirect:/admin/price-location-taxes";
    }

    @GetMapping("/price-location-taxes/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var item = service.getById(id);
        model.addAttribute("form", new PriceLocationTaxForm(item.id(), item.locationTax(), item.locationId()));
        model.addAttribute("locations", locationRepository.findAll());
        return "price-location-tax-form";
    }

    @PostMapping("/price-location-taxes/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("form") PriceLocationTaxForm form) {
        service.update(id, new PriceLocationTaxRequest(form.getLocationTax(), form.getLocationId()));
        return "redirect:/admin/price-location-taxes";
    }

    @PostMapping("/price-location-taxes/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/admin/price-location-taxes";
    }
}
