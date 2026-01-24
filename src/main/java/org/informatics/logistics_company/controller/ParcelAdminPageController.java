package org.informatics.logistics_company.controller;

import org.informatics.logistics_company.dto.parcel.ParcelForm;
import org.informatics.logistics_company.dto.parcel.ParcelRequest;
import org.informatics.logistics_company.repository.*;
import org.informatics.logistics_company.service.ParcelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class ParcelAdminPageController {

    private final ParcelService parcelService;

    private final LocationRepository locationRepository;
    private final PriceLocationTaxRepository priceLocationTaxRepository;
    private final PriceWeightTaxRepository priceWeightTaxRepository;
    private final StaffRepository staffRepository;
    private final UserDetailsRepository userDetailsRepository;

    public ParcelAdminPageController(
            ParcelService parcelService,
            LocationRepository locationRepository,
            PriceLocationTaxRepository priceLocationTaxRepository,
            PriceWeightTaxRepository priceWeightTaxRepository,
            StaffRepository staffRepository,
            UserDetailsRepository userDetailsRepository
    ) {
        this.parcelService = parcelService;
        this.locationRepository = locationRepository;
        this.priceLocationTaxRepository = priceLocationTaxRepository;
        this.priceWeightTaxRepository = priceWeightTaxRepository;
        this.staffRepository = staffRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    @GetMapping("/parcels")
    public String list(@RequestParam(defaultValue = "false") boolean hideDelivered, @RequestParam(required = false) String q, Model model) {
        model.addAttribute("parcels", parcelService.fetchAllAdminParcels(hideDelivered));
        model.addAttribute("hideDelivered", hideDelivered);
        model.addAttribute("q", q);
        return "parcel-admin";
    }

    @GetMapping("/parcels/new")
    public String createForm(Model model) {
        ParcelForm form = new ParcelForm();
        model.addAttribute("form", form);
        fillDropdowns(model);
        return "parcel-admin-form";
    }

    @PostMapping("/parcels")
    public String create(@ModelAttribute("form") ParcelForm form) {
        parcelService.create(toRequest(form));
        return "redirect:/admin/parcels";
    }

    @GetMapping("/parcels/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var p = parcelService.getById(id);

        ParcelForm form = new ParcelForm();
        form.setId(p.id());
        form.setTrackingNumber(p.trackingNumber());
        form.setWeight(p.weight());
        form.setPrice(p.price());
        form.setSentDate(p.sentDate());
        form.setReceivedDate(p.receivedDate());
        form.setParcelStatus(p.parcelStatus());

        form.setSendLocationId(p.sendLocationId());
        form.setReceiverLocationId(p.receiverLocationId());
        form.setSenderUserId(p.senderUserId());
        form.setReceiverUserId(p.receiverUserId());
        form.setPriceLocationTaxId(p.priceLocationTaxId());
        form.setPriceWeightTaxId(p.priceWeightTaxId());
        form.setStaffId(p.staffId());

        model.addAttribute("form", form);
        fillDropdowns(model);
        return "parcel-admin-form";
    }

    @PostMapping("/parcels/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("form") ParcelForm form) {
        parcelService.update(id, toRequest(form));
        return "redirect:/admin/parcels";
    }

    @PostMapping("/parcels/{id}/delete")
    public String delete(@PathVariable Long id) {
        parcelService.delete(id);
        return "redirect:/admin/parcels";
    }

    private ParcelRequest toRequest(ParcelForm f) {
        return new ParcelRequest(
                f.getWeight(),
                f.getPrice(),
                f.getSentDate(),
                f.getReceivedDate(),
                f.getSendLocationId(),
                f.getReceiverLocationId(),
                f.getParcelStatus(),
                f.getSenderUserId(),
                f.getReceiverUserId(),
                f.getPriceLocationTaxId(),
                f.getPriceWeightTaxId(),
                f.getStaffId()
        );
    }

    private void fillDropdowns(Model model) {
        model.addAttribute("locations", locationRepository.findAll());
        model.addAttribute("locationTaxes", priceLocationTaxRepository.findAll());
        model.addAttribute("weightTaxes", priceWeightTaxRepository.findAll());
        model.addAttribute("staffList", staffRepository.findAll());
        model.addAttribute("users", userDetailsRepository.findAll());
        // ParcelStatus enum values:
        model.addAttribute("statuses", org.informatics.logistics_company.model.enums.ParcelStatus.values());
    }
}
