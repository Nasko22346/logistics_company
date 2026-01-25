package org.informatics.logistics_company.controller;

import org.informatics.logistics_company.dto.client.ClientForm;
import org.informatics.logistics_company.model.enums.Role;
import org.informatics.logistics_company.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/clients")
public class ClientPageController {

    private final ClientService clientService;

    public ClientPageController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model,
                       @ModelAttribute("error") String error,
                       @ModelAttribute("success") String success) {
        model.addAttribute("clients", clientService.listClients(q));
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("roles", Role.values());
        return "clients";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new ClientForm());
        model.addAttribute("roles", Role.values());
        return "client-form";
    }

    @PostMapping
    public String create(@ModelAttribute("form") ClientForm form, RedirectAttributes ra, Model model) {
        try {
            clientService.create(form);
            ra.addFlashAttribute("success", "Client created successfully.");
            return "redirect:/admin/clients";
        } catch (Exception e) {
            model.addAttribute("roles", Role.values());
            model.addAttribute("error", e.getMessage());
            return "client-form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("form", clientService.getClientForm(id));
        model.addAttribute("roles", Role.values());
        return "client-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("form") ClientForm form,
                         RedirectAttributes ra, Model model) {
        try {
            clientService.update(id, form);
            ra.addFlashAttribute("success", "Client updated successfully.");
            return "redirect:/admin/clients";
        } catch (Exception e) {
            model.addAttribute("roles", Role.values());
            model.addAttribute("error", e.getMessage());
            return "client-form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            clientService.delete(id);
            ra.addFlashAttribute("success", "Client deleted successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/clients";
    }
}
