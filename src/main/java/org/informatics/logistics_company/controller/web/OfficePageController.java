package org.informatics.logistics_company.controller.web;

import org.informatics.logistics_company.repository.OfficeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OfficePageController {

    private final OfficeRepository officeRepository;

    public OfficePageController(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    @GetMapping("/admin/offices")
    public String offices(Model model) {
        model.addAttribute("offices", officeRepository.findAll());
        return "offices"; // templates/offices.html
    }
}
