package org.informatics.logistics_company.service;

import org.informatics.logistics_company.model.enums.ParcelStatus;
import org.informatics.logistics_company.repository.LocationRepository;
import org.informatics.logistics_company.repository.PriceLocationTaxRepository;
import org.informatics.logistics_company.repository.StaffRepository;
import org.informatics.logistics_company.repository.UserDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
public class DropdownService {


    private final LocationRepository locationRepository;
    private final PriceLocationTaxRepository priceLocationTaxRepository;
    private final PriceLocationTaxRepository priceWeightTaxRepository;
    private final StaffRepository staffRepository;
    private final UserDetailsRepository userDetailsRepository;

    public DropdownService(LocationRepository locationRepository, PriceLocationTaxRepository priceLocationTaxRepository,
                           PriceLocationTaxRepository priceWeightTaxRepository,
                           StaffRepository staffRepository, UserDetailsRepository userDetailsRepository) {
        this.locationRepository = locationRepository;
        this.priceLocationTaxRepository = priceLocationTaxRepository;
        this.priceWeightTaxRepository = priceWeightTaxRepository;
        this.staffRepository = staffRepository;
        this.userDetailsRepository = userDetailsRepository;
    }


    public void fillDropdowns(Model model) {
        model.addAttribute("locations", locationRepository.findAll());
        model.addAttribute("locationTaxes", priceLocationTaxRepository.findAll());
        model.addAttribute("weightTaxes", priceWeightTaxRepository.findAll());
        model.addAttribute("staffList", staffRepository.findAll());
        model.addAttribute("users", userDetailsRepository.findAll());
        // ParcelStatus enum values:
        model.addAttribute("statuses", ParcelStatus.values());
    }

    public List<String> createStaffDropdown() {
        List<String> staffNames = new ArrayList<>();
        this.staffRepository.findAll().forEach(staff -> staffNames.add(staff.getStaffUserDetails().getFirstName() + " " + staff.getStaffUserDetails().getLastName()));
        return staffNames;
    }

    public List<String> cerateOfficeLocationDropdown() {
        List<String> locationNames = new ArrayList<>();
        this.locationRepository.findAll().forEach(location -> locationNames.add(location.getLocationRegion() + ", " + location.getLocationCountry()));
        return locationNames;
    }
}
