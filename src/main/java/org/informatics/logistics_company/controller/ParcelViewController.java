package org.informatics.logistics_company.controller;

import jakarta.validation.Valid;
import org.informatics.logistics_company.dto.parcel.ParcelRequest;
import org.informatics.logistics_company.service.ParcelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/parcels")
public class ParcelViewController {

    private final ParcelService parcelService;

    public ParcelViewController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    @GetMapping("/all")
    public String getAllParcels(Model model) {
        model.addAttribute("parcels", parcelService.fetchAllParcels());
        return "parcels";
    }

    @GetMapping("/client/{clientId}")
    public String getClientParcels(@PathVariable Long clientId, Model model) {
        model.addAttribute("parcels", parcelService.fetchParcelsForClient(clientId));
        model.addAttribute("clientId", clientId);
        return "client_parcels";
    }

    @GetMapping("/create")
    public String showCreateParcelForm(Model model) {
        model.addAttribute("parcelRequest", new ParcelRequest());
        return "create_parcel";
    }

    @PostMapping("/create")
    public String createParcel(@Valid @ModelAttribute ParcelRequest parcelRequest, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "create_parcel";
        }

        try {
            parcelService.createParcel(parcelRequest);
            return "redirect:/api/v1/parcels/all";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating parcel: " + e.getMessage());
            return "create_parcel";
        }
    }
}
