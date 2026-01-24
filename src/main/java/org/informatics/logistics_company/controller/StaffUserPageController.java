package org.informatics.logistics_company.controller;

import jakarta.validation.Valid;
import org.informatics.logistics_company.dto.staff.StaffUserForm;
import org.informatics.logistics_company.model.enums.Position;
import org.informatics.logistics_company.model.jpa.Staff;
import org.informatics.logistics_company.repository.OfficeRepository;
import org.informatics.logistics_company.service.StaffUserAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
public class StaffUserPageController {

    private final StaffUserAdminService staffUserAdminService;
    private final OfficeRepository officeRepository;

    public StaffUserPageController(StaffUserAdminService staffUserAdminService, OfficeRepository officeRepository) {
        this.staffUserAdminService = staffUserAdminService;
        this.officeRepository = officeRepository;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("users", staffUserAdminService.list(q));
        model.addAttribute("q", q);
        return "users";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new StaffUserForm());
        model.addAttribute("offices", officeRepository.findAll());
        model.addAttribute("positions", Position.values());
        return "user-form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("form") StaffUserForm form,
            BindingResult br,
            Model model
    ) {
        // password required on create:
        if (form.getPassword() == null || form.getPassword().isBlank()) {
            br.rejectValue("password", "required", "Password е задължителна при създаване.");
        }

        if (br.hasErrors()) {
            model.addAttribute("offices", officeRepository.findAll());
            model.addAttribute("positions", Position.values());
            return "user-form";
        }

        try {
            staffUserAdminService.create(form);
        } catch (IllegalArgumentException ex) {
            br.reject("createFailed", ex.getMessage());
            model.addAttribute("offices", officeRepository.findAll());
            model.addAttribute("positions", Position.values());
            return "user-form";
        }

        return "redirect:/admin/users";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Staff staff = staffUserAdminService.getById(id);

        StaffUserForm form = new StaffUserForm();
        form.setStaffId(staff.getStaffId());
        form.setOfficeId(staff.getOffice().getOfficeId());
        form.setPosition(staff.getPosition());
        form.setFirstName(staff.getStaffUserDetails().getFirstName());
        form.setMiddleName(staff.getStaffUserDetails().getMiddleName());
        form.setLastName(staff.getStaffUserDetails().getLastName());
        form.setPhoneNumber(staff.getStaffUserDetails().getPhoneNumber());
        form.setEmail(staff.getStaffUserDetails().getLoginDetails().getEmail());
        form.setPassword(""); // празно -> няма промяна

        model.addAttribute("form", form);
        model.addAttribute("offices", officeRepository.findAll());
        model.addAttribute("positions", Position.values());
        return "user-form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") StaffUserForm form,
            BindingResult br,
            Model model
    ) {
        if (br.hasErrors()) {
            model.addAttribute("offices", officeRepository.findAll());
            model.addAttribute("positions", Position.values());
            return "user-form";
        }

        try {
            staffUserAdminService.update(id, form);
        } catch (IllegalArgumentException ex) {
            br.reject("updateFailed", ex.getMessage());
            model.addAttribute("offices", officeRepository.findAll());
            model.addAttribute("positions", Position.values());
            return "user-form";
        }

        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        staffUserAdminService.delete(id);
        return "redirect:/admin/users";
    }
}
