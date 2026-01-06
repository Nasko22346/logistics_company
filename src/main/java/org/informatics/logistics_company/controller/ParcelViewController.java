package org.informatics.logistics_company.controller;

import org.informatics.logistics_company.service.ParcelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
