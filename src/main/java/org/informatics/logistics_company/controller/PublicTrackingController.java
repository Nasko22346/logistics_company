package org.informatics.logistics_company.controller;

import org.informatics.logistics_company.dto.parcel.ParcelResponse;
import org.informatics.logistics_company.service.ParcelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PublicTrackingController {

    private final ParcelService parcelService;

    public PublicTrackingController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    @GetMapping("/track-parcel")
    public String trackParcel(@RequestParam(required = false) Long trackingNumber, Model model) {
        if (trackingNumber != null) {
            try {
                ParcelResponse parcel = parcelService.fetchParcelByID(trackingNumber);
                model.addAttribute("parcel", parcel);
                model.addAttribute("trackingNumber", trackingNumber);
            } catch (RuntimeException e) {
                model.addAttribute("error", "Parcel with tracking number " + trackingNumber + " not found.");
                model.addAttribute("trackingNumber", trackingNumber);
            }
        }
        return "track-parcel";
    }
}
