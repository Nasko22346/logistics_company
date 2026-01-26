package org.informatics.logistics_company.controller;

import jakarta.validation.Valid;
import org.informatics.logistics_company.dto.office.OfficeForm;
import org.informatics.logistics_company.dto.office.OfficeRequest;
import org.informatics.logistics_company.exception.OfficeValidationException;
import org.informatics.logistics_company.service.OfficeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin")
public class OfficePageController {
    private final OfficeService officeService;

    public OfficePageController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping("/offices")
    public String list(@RequestParam(required = false) Long companyId,
                       @RequestParam(required = false) String q,
                       Model model) {

        model.addAttribute("offices", officeService.search(q, companyId));
        model.addAttribute("companyId", companyId);
        model.addAttribute("q", q);

        if (companyId != null) {
            model.addAttribute("companyName", officeService.getCompanyNameById(companyId).orElse(null));
        } else {
            model.addAttribute("companyName", null);
        }

        return "offices";
    }

    @GetMapping("/offices/new")
    public String createForm(Model model) {
        model.addAttribute("form", new OfficeForm());
        model.addAttribute("companies", officeService.getAllCompanies());
        model.addAttribute("openTimes", officeService.getAllOpenTimes());
        model.addAttribute("locations", officeService.getAllLocations());
        return "office-form";
    }

    @PostMapping("/offices")
    public String create(@Valid @ModelAttribute("form") OfficeForm form, BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("companies", officeService.getAllCompanies());
            model.addAttribute("openTimes", officeService.getAllOpenTimes());
            model.addAttribute("locations", officeService.getAllLocations());
            return "office-form";
        }

        try {
            officeService.create(new OfficeRequest(
                    form.getOfficePhone(),
                    form.getOfficeEmail(),
                    form.getCompanyId(),
                    form.getOpenTimeId(),
                    form.getLocationId()
            ));
            return "redirect:/admin/offices";
        } catch (OfficeValidationException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("companies", officeService.getAllCompanies());
            model.addAttribute("openTimes", officeService.getAllOpenTimes());
            model.addAttribute("locations", officeService.getAllLocations());
            return "office-form";
        }
    }

    @GetMapping("/offices/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var office = officeService.getById(id);

        Long openTimeId = office.openTime() != null ? office.openTime().getWorkTimeId() : null;
        Long locationId = office.location() != null ? office.location().getLocationId() : null;

        model.addAttribute("form", new OfficeForm(
                office.officeId(),
                office.officePhone(),
                office.officeEmail(),
                office.companyId(),
                openTimeId,
                locationId
        ));

        model.addAttribute("companies", officeService.getAllCompanies());
        model.addAttribute("openTimes", officeService.getAllOpenTimes());
        model.addAttribute("locations", officeService.getAllLocations());

        return "office-form";
    }

    @PostMapping("/offices/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") OfficeForm form, BindingResult br, Model model) {
        if (br.hasErrors()) {
            form.setId(id);
            model.addAttribute("companies", officeService.getAllCompanies());
            model.addAttribute("openTimes", officeService.getAllOpenTimes());
            model.addAttribute("locations", officeService.getAllLocations());
            return "office-form";
        }

        try {
            officeService.update(id, new OfficeRequest(
                    form.getOfficePhone(),
                    form.getOfficeEmail(),
                    form.getCompanyId(),
                    form.getOpenTimeId(),
                    form.getLocationId()
            ));
            return "redirect:/admin/offices";
        } catch (OfficeValidationException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("companies", officeService.getAllCompanies());
            model.addAttribute("openTimes", officeService.getAllOpenTimes());
            model.addAttribute("locations", officeService.getAllLocations());
            return "office-form";
        }
    }

    @PostMapping("/offices/{id}/delete")
    public String delete(@PathVariable Long id) {
        officeService.delete(id);
        return "redirect:/admin/offices";
    }
}
