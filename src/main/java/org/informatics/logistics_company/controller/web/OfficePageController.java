package org.informatics.logistics_company.controller.web;

import org.informatics.logistics_company.dto.office.OfficeForm;
import org.informatics.logistics_company.dto.office.OfficeRequest;
import org.informatics.logistics_company.repository.CompanyRepository;
import org.informatics.logistics_company.repository.OfficeRepository;
import org.informatics.logistics_company.service.OfficeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class OfficePageController {

    private final OfficeService officeService;
    private final CompanyRepository companyRepository;
    private final OfficeRepository officeRepository;

    public OfficePageController(OfficeService officeService, CompanyRepository companyRepository, OfficeRepository officeRepository) {
        this.officeService = officeService;
        this.companyRepository = companyRepository;
        this.officeRepository = officeRepository;
    }

    @GetMapping("/offices")
    public String list(@RequestParam(required = false) Long companyId,
                       @RequestParam(required = false) String q,
                       Model model) {

        model.addAttribute("offices", officeService.search(q, companyId));
        model.addAttribute("companyId", companyId);
        model.addAttribute("q", q);

        if (companyId != null) {
            var company = companyRepository.findById(companyId).orElse(null);
            model.addAttribute("companyName", company != null ? company.getCompanyName() : null);
        } else {
            model.addAttribute("companyName", null);
        }

        return "offices";
    }

    @GetMapping("/offices/new")
    public String createForm(Model model) {
        model.addAttribute("form", new OfficeForm());
        model.addAttribute("companies", companyRepository.findAll());
        return "office-form";
    }

    @PostMapping("/offices")
    public String create(@ModelAttribute("form") OfficeForm form) {
        officeService.create(new OfficeRequest(form.getOfficePhone(), form.getOfficeEmail(), form.getCompanyId()));
        return "redirect:/admin/offices";
    }

    @GetMapping("/offices/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var office = officeService.getById(id);
        model.addAttribute("form", new OfficeForm(
                office.officeId(),
                office.officePhone(),
                office.officeEmail(),
                office.companyId()
        ));
        model.addAttribute("companies", companyRepository.findAll());
        return "office-form";
    }

    @PostMapping("/offices/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("form") OfficeForm form) {
        officeService.update(id, new OfficeRequest(form.getOfficePhone(), form.getOfficeEmail(), form.getCompanyId()));
        return "redirect:/admin/offices";
    }

    @PostMapping("/offices/{id}/delete")
    public String delete(@PathVariable Long id) {
        officeService.delete(id);
        return "redirect:/admin/offices";
    }
}
