package org.informatics.logistics_company.controller;

import org.informatics.logistics_company.dto.parcel.ParcelForm;
import org.informatics.logistics_company.dto.parcel.ParcelRequest;
import org.informatics.logistics_company.dto.parcel.ParcelResponse;
import org.informatics.logistics_company.service.DropdownService;
import org.informatics.logistics_company.service.ParcelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class ParcelAdminPageController {

    private final ParcelService parcelService;
    private final DropdownService dropdownService;

    public ParcelAdminPageController(ParcelService parcelService, DropdownService dropdownService) {
        this.parcelService = parcelService;
        this.dropdownService = dropdownService;
    }

    @GetMapping("/parcels")
    public String list(@RequestParam(required = false) String q, @RequestParam(defaultValue = "false")
    boolean hideDelivered, Model model) {
        model.addAttribute("parcels", parcelService.fetchAllAdminParcels(q, hideDelivered));
        model.addAttribute("q", q);
        model.addAttribute("hideDelivered", hideDelivered);

        return "parcel-admin";
    }

    @GetMapping("/parcels/new")
    public String createForm(Model model) {
        ParcelForm form = new ParcelForm();
        model.addAttribute("form", form);
        this.dropdownService.fillDropdowns(model);

        return "parcel-admin-form";
    }

    @PostMapping("/parcels")
    public String create(@ModelAttribute("form") ParcelRequest request) {
        parcelService.create(request);

        return "redirect:/admin/parcels";
    }

    @GetMapping("/parcels/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        ParcelResponse parcelResponse = parcelService.getById(id);
        model.addAttribute("form", parcelResponse);
        this.dropdownService.fillDropdowns(model);

        return "parcel-admin-form";
    }

    @PostMapping("/parcels/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("request") ParcelRequest request) {
        parcelService.update(id, request);

        return "redirect:/admin/parcels";
    }

    @PostMapping("/parcels/{id}/delete")
    public String delete(@PathVariable Long id) {
        parcelService.delete(id);

        return "redirect:/admin/parcels";
    }
}
