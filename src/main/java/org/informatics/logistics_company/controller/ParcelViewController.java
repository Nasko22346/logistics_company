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
import org.springframework.web.bind.annotation.RequestParam;

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
        return "create_parcel";
    }

    @PostMapping("/create")
    public String createParcel(@Valid @ModelAttribute ParcelRequest parcelRequest, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "create_parcel";
        }

        try {
            parcelService.createParcel(parcelRequest);
            return "redirect:/parcels/all";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating parcel: " + e.getMessage());
            return "create_parcel";
        }
    }

    @GetMapping("/{parcelId}")
    public String getParcelDetails(@PathVariable Long parcelId, Model model) {
        model.addAttribute("parcel", parcelService.fetchParcelByID(parcelId));
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

    //TODO: Merge both endpoints below into one with a parameter indicating sender/receiver
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

    // Form submission handlers - redirect to path variable endpoints

    @GetMapping("/by-employee")
    public String filterByEmployeeForm(@RequestParam Long employeeId) {
        return "redirect:/api/v1/parcels/by-employee/" + employeeId;
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


//2026-01-24T10:22:45.848+02:00  WARN 46948 --- [LogisticsCompany] [io-8080-exec-10] o.s.s.c.bcrypt.BCryptPasswordEncoder     : Encoded password does not look like BCrypt
//2026-01-24T10:22:45.849+02:00 DEBUG 46948 --- [LogisticsCompany] [io-8080-exec-10] o.s.s.a.dao.DaoAuthenticationProvider    : Failed to authenticate since password does not match stored value
//2026-01-24T10:22:45.849+02:00 DEBUG 46948 --- [LogisticsCompany] [io-8080-exec-10] o.s.s.authentication.ProviderManager     : Authentication failed with provider DaoAuthenticationProvider since Bad credentials
//2026-01-24T10:22:45.849+02:00 DEBUG 46948 --- [LogisticsCompany] [io-8080-exec-10] o.s.s.authentication.ProviderManager     : Denying authentication since all attempted providers failed
//2026-01-24T10:22:45.850+02:00 DEBUG 46948 --- [LogisticsCompany] [io-8080-exec-10] o.s.s.web.DefaultRedirectStrategy        : Redirecting to /?error
//2026-01-24T10:22:45.883+02:00 DEBUG 46948 --- [LogisticsCompany] [nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Securing GET /?error
//2026-01-24T10:22:45.884+02:00 DEBUG 46948 --- [LogisticsCompany] [nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Secured GET /?error
//2026-01-24T10:22:45.886+02:00 DEBUG 46948 --- [LogisticsCompany] [nio-8080-exec-1] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
//2026-01-24T10:22:45.909+02:00 DEBUG 46948 --- [LogisticsCompany] [nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Securing GET /css/app.css
//2026-01-24T10:22:45.909+02:00 DEBUG 46948 --- [LogisticsCompany] [nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Secured GET /css/app.css
//2026-01-24T10:22:45.913+02:00 DEBUG 46948 --- [LogisticsCompany] [nio-8080-exec-2] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext