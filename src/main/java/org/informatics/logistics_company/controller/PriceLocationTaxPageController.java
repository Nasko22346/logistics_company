package org.informatics.logistics_company.controller;

import jakarta.validation.Valid;
import org.informatics.logistics_company.dto.price_location_tax.PriceLocationTaxForm;
import org.informatics.logistics_company.dto.price_location_tax.PriceLocationTaxRequest;
import org.informatics.logistics_company.service.PriceLocationTaxService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class PriceLocationTaxPageController {

    private final PriceLocationTaxService service;

    public PriceLocationTaxPageController(PriceLocationTaxService service) {
        this.service = service;
    }

    @GetMapping("/price-location-taxes")
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("items", service.search(q));
        model.addAttribute("q", q);
        return "price-location-taxes";
    }

    @GetMapping("/price-location-taxes/new")
    public String createForm(Model model) {
        model.addAttribute("form", new PriceLocationTaxForm());
        model.addAttribute("locations", service.getAllLocations());
        return "price-location-tax-form";
    }

    @PostMapping("/price-location-taxes")
    public String create(@Valid @ModelAttribute("form") PriceLocationTaxForm form, BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("locations", service.getAllLocations());
            return "price-location-tax-form";
        }
        service.create(new PriceLocationTaxRequest(form.getLocationTax(), form.getLocationId()));
        return "redirect:/admin/price-location-taxes";
    }

    @GetMapping("/price-location-taxes/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var item = service.getById(id);
        model.addAttribute("form", new PriceLocationTaxForm(item.id(), item.locationTax(), item.locationId()));
        model.addAttribute("locations", service.getAllLocations());
        return "price-location-tax-form";
    }

    @PostMapping("/price-location-taxes/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") PriceLocationTaxForm form, BindingResult br, Model model) {
        if (br.hasErrors()) {
            form.setId(id);
            model.addAttribute("locations", service.getAllLocations());
            return "price-location-tax-form";
        }
        service.update(id, new PriceLocationTaxRequest(form.getLocationTax(), form.getLocationId()));
        return "redirect:/admin/price-location-taxes";
    }

    @PostMapping("/price-location-taxes/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/admin/price-location-taxes";
    }
}
