package org.informatics.logistics_company.controller;

import jakarta.validation.Valid;
import org.informatics.logistics_company.dto.price_weight_tax.PriceWeightTaxForm;
import org.informatics.logistics_company.service.PriceWeightTaxService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class PriceWeightTaxPageController {
    private final PriceWeightTaxService service;

    public PriceWeightTaxPageController(PriceWeightTaxService service) {
        this.service = service;
    }

    @GetMapping("/price-weight-taxes")
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("items", service.search(q));
        model.addAttribute("q", q);
        return "price-weight-taxes";
    }

    @GetMapping("/price-weight-taxes/new")
    public String createForm(Model model) {
        model.addAttribute("form", new PriceWeightTaxForm());
        return "price-weight-tax-form";
    }

    @PostMapping("/price-weight-taxes")
    public String create(@Valid @ModelAttribute("form") PriceWeightTaxForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            return "price-weight-tax-form";
        }

        service.create(form);
        return "redirect:/admin/price-weight-taxes";
    }

    @GetMapping("/price-weight-taxes/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var e = service.getById(id);
        model.addAttribute("form", new PriceWeightTaxForm(
                e.getId(),
                e.getMaxWeightAmount(),
                e.getWeightTax()
        ));
        return "price-weight-tax-form";
    }

    @PostMapping("/price-weight-taxes/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") PriceWeightTaxForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            return "price-weight-tax-form";
        }

        service.update(id, form);
        return "redirect:/admin/price-weight-taxes";
    }

    @PostMapping("/price-weight-taxes/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/admin/price-weight-taxes";
    }
}
