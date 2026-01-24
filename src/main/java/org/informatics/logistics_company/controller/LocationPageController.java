package org.informatics.logistics_company.controller;

import jakarta.validation.Valid;
import org.informatics.logistics_company.dto.location.LocationForm;
import org.informatics.logistics_company.dto.location.LocationRequest;
import org.informatics.logistics_company.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class LocationPageController {

    private final LocationService locationService;

    public LocationPageController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/locations")
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("locations", locationService.search(q));
        model.addAttribute("q", q == null ? "" : q);
        return "locations";
    }

    @GetMapping("/locations/new")
    public String createForm(Model model) {
        model.addAttribute("form", new LocationForm());
        return "location-form";
    }

    @PostMapping("/locations")
    public String create(@Valid @ModelAttribute("form") LocationForm form, BindingResult br) {
        if (br.hasErrors()) {
            return "location-form";
        }
        locationService.create(new LocationRequest(
                form.getLocationCountry(),
                form.getProvince(),
                form.getLocationRegion(),
                form.getLocationDescription()
        ));
        return "redirect:/admin/locations";
    }

    @GetMapping("/locations/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var loc = locationService.getById(id);
        model.addAttribute("form", new LocationForm(
                loc.locationId(),
                loc.locationCountry(),
                loc.province(),
                loc.locationRegion(),
                loc.locationDescription()
        ));
        return "location-form";
    }

    @PostMapping("/locations/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") LocationForm form, BindingResult br) {
        if (br.hasErrors()) {
            form.setId(id);
            return "location-form";
        }
        locationService.update(id, new LocationRequest(
                form.getLocationCountry(),
                form.getProvince(),
                form.getLocationRegion(),
                form.getLocationDescription()
        ));
        return "redirect:/admin/locations";
    }

    @PostMapping("/locations/{id}/delete")
    public String delete(@PathVariable Long id) {
        locationService.delete(id);
        return "redirect:/admin/locations";
    }
}
