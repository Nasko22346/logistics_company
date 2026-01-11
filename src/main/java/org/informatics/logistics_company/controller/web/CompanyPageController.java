package org.informatics.logistics_company.controller.web;

import org.informatics.logistics_company.dto.company.CompanyForm;
import org.informatics.logistics_company.dto.company.CompanyRequest;
import org.informatics.logistics_company.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class CompanyPageController {

    private final CompanyService companyService;

    public CompanyPageController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companies")
    public String list(Model model) {
        model.addAttribute("companies", companyService.loadData());
        return "companies";
    }

    @GetMapping("/companies/new")
    public String createForm(Model model) {
        model.addAttribute("form", new CompanyForm());
        return "company-form";
    }

    @PostMapping("/companies")
    public String create(@ModelAttribute("form") CompanyForm form) {
        companyService.create(new CompanyRequest(
                form.getCompanyName(),
                form.getPhoneNumber(),
                form.getEmail(),
                form.getCompanyEik(),
                form.getCompanyDescription()
        ));
        return "redirect:/admin/companies";
    }

    @GetMapping("/companies/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var c = companyService.getById(id);
        model.addAttribute("form", new CompanyForm(
                c.companyId(),
                c.companyName(),
                c.phoneNumber(),
                c.email(),
                c.companyEik(),
                c.companyDescription()
        ));
        return "company-form";
    }

    @PostMapping("/companies/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("form") CompanyForm form) {
        companyService.update(id, new CompanyRequest(
                form.getCompanyName(),
                form.getPhoneNumber(),
                form.getEmail(),
                form.getCompanyEik(),
                form.getCompanyDescription()
        ));
        return "redirect:/admin/companies";
    }

    @PostMapping("/companies/{id}/delete")
    public String delete(@PathVariable Long id) {
        companyService.delete(id);
        return "redirect:/admin/companies";
    }
}
