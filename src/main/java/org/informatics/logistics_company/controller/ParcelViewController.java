package org.informatics.logistics_company.controller;

import jakarta.validation.Valid;
import org.informatics.logistics_company.dto.parcel.ParcelRequest;
import org.informatics.logistics_company.dto.parcel.PriceCalculationResponse;
import org.informatics.logistics_company.service.ParcelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/parcels")
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

    @GetMapping("/get-create-form")
    public String showCreateParcelForm(Model model) {
        model.addAttribute("parcelRequest", new ParcelRequest());
        model.addAttribute("parcelResponseWithDefaults", parcelService.getParcelFormWithDefaults());
        return "create_parcel";
    }

    @PostMapping("/create")
    public String createParcel(@Valid @ModelAttribute ParcelRequest parcelRequest, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("parcelResponseWithDefaults", parcelService.getParcelFormWithDefaults());
            return "create_parcel";
        }

        try {
            String trackingNumber = parcelService.createParcel(parcelRequest);
            return "redirect:/parcels/confirmation?trackingNumber=" + trackingNumber;
        } catch (Exception e) {
            model.addAttribute("error", "Error creating parcel: " + e.getMessage());
            model.addAttribute("parcelResponseWithDefaults", parcelService.getParcelFormWithDefaults());
            return "create_parcel";
        }
    }

    @GetMapping("/confirmation")
    public String showConfirmation(@RequestParam String trackingNumber, Model model) {
        model.addAttribute("trackingNumber", trackingNumber);
        return "parcel_confirmation";
    }

    @GetMapping("/calculate-price")
    @ResponseBody
    public PriceCalculationResponse calculatePrice(
            @RequestParam(required = false) Double weight,
            @RequestParam(required = false) String deliveryType) {
        return parcelService.calculatePriceEstimate(weight, deliveryType);
    }

    @GetMapping("/{parcelId}")
    public String getParcelDetails(@PathVariable Long parcelId, Model model) {
        model.addAttribute("parcelByID", parcelService.fetchParcelByID(parcelId));
        return "parcel_details";
    }

    // Filter endpoints - logic to be implemented later

    @GetMapping("/by-employee/{employeeId}")
    public String getParcelsByEmployee(@PathVariable Long employeeId, Model model) {
        model.addAttribute("parcels", parcelService.fetchParcelsByStaff(employeeId));
        model.addAttribute("filterType", "by-employee");
        model.addAttribute("filterId", employeeId);
        return "parcels";
    }

    @GetMapping("/not-received")
    public String getParcelsNotReceived(Model model) {
        model.addAttribute("parcels", parcelService.fetchAllNotDeliveredParcels());
        model.addAttribute("filterType", "not-received");
        return "parcels";
    }

    @GetMapping("/by-sender/{senderId}")
    public String getParcelsBySender(@PathVariable Long senderId, Model model) {
        model.addAttribute("parcels", parcelService.fetchParcelsBySender(senderId));
        model.addAttribute("filterType", "by-sender");
        model.addAttribute("filterId", senderId);
        return "parcels";
    }

    @GetMapping("/by-receiver/{receiverId}")
    public String getParcelsByReceiver(@PathVariable Long receiverId, Model model) {
        model.addAttribute("parcels", parcelService.fetchParcelsByReceiver(receiverId));
        model.addAttribute("filterType", "by-receiver");
        model.addAttribute("filterId", receiverId);
        return "parcels";
    }

    @GetMapping("/by-employee")
    public String filterByEmployeeForm(@RequestParam Long employeeId) {
        return "redirect:/parcels/by-employee/" + employeeId;
    }

    @GetMapping("/by-sender")
    public String filterBySenderForm(@RequestParam Long senderId) {
        return "redirect:/parcels/by-sender/" + senderId;
    }

    @GetMapping("/by-receiver")
    public String filterByReceiverForm(@RequestParam Long receiverId) {
        return "redirect:/parcels/by-receiver/" + receiverId;
    }
}
