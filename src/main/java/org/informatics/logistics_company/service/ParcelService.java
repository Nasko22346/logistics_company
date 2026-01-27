package org.informatics.logistics_company.service;

import org.informatics.logistics_company.dto.parcel.ParcelAdminRow;
import org.informatics.logistics_company.dto.parcel.ParcelRequest;
import org.informatics.logistics_company.dto.parcel.ParcelResponse;
import org.informatics.logistics_company.dto.parcel.PriceCalculationResponse;
import org.informatics.logistics_company.dto.reports.RevenueReport;
import org.informatics.logistics_company.dto.reports.RevenueRow;
import org.informatics.logistics_company.exception.ParcelNotFoundException;
import org.informatics.logistics_company.model.enums.ParcelStatus;
import org.informatics.logistics_company.model.jpa.Location;
import org.informatics.logistics_company.model.jpa.Parcel;
import org.informatics.logistics_company.model.jpa.PriceLocationTax;
import org.informatics.logistics_company.model.jpa.PriceWeightTax;
import org.informatics.logistics_company.model.jpa.Staff;
import org.informatics.logistics_company.model.jpa.UserDetails;
import org.informatics.logistics_company.repository.LocationRepository;
import org.informatics.logistics_company.repository.ParcelRepository;
import org.informatics.logistics_company.repository.PriceLocationTaxRepository;
import org.informatics.logistics_company.repository.PriceWeightTaxRepository;
import org.informatics.logistics_company.repository.StaffRepository;
import org.informatics.logistics_company.repository.UserDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.informatics.logistics_company.exception.ExceptionMessages.PARCEL_NOT_FOUND;

@Service
public class ParcelService {

    private final ParcelRepository parcelRepository;
    private final DropdownService dropdownService;
    private final UserDetailsRepository userDetailsRepository;
    private final StaffRepository staffRepository;
    private final LocationRepository locationRepository;
    private final PriceWeightTaxRepository priceWeightTaxRepository;
    private final PriceLocationTaxRepository priceLocationTaxRepository;

    public ParcelService(ParcelRepository parcelRepository, DropdownService dropdownService,
                         UserDetailsRepository userDetailsRepository, StaffRepository staffRepository,
                         LocationRepository locationRepository, PriceWeightTaxRepository priceWeightTaxRepository,
                         PriceLocationTaxRepository priceLocationTaxRepository) {
        this.parcelRepository = parcelRepository;
        this.dropdownService = dropdownService;
        this.userDetailsRepository = userDetailsRepository;
        this.staffRepository = staffRepository;
        this.locationRepository = locationRepository;
        this.priceWeightTaxRepository = priceWeightTaxRepository;
        this.priceLocationTaxRepository = priceLocationTaxRepository;
    }

    private static List<ParcelResponse> getParcelResponses(List<Parcel> parcels) {
        List<ParcelResponse> responses = new ArrayList<>();
        parcels.forEach(parcel -> {
            ParcelResponse parcelResponse = MapperService.mapToParcelResponse(parcel);
            responses.add(parcelResponse);
        });

        return responses;
    }

    //TODO: Check if needed
    public List<ParcelResponse> listAll(String q) {
        var list = (q != null && !q.isBlank())
                ? parcelRepository.findByTrackingNumberContainingIgnoreCaseOrderByIdDesc(q.trim())
                : parcelRepository.findAllByOrderByIdDesc();

        return list.stream().map(MapperService::mapToParcelResponse).toList();
    }

    public ParcelResponse getById(Long id) {
        Parcel parcel = parcelRepository.findWithAllById(id).orElseThrow(() -> new ParcelNotFoundException(PARCEL_NOT_FOUND + id));
        return MapperService.mapToParcelResponse(parcel);
    }

    public ParcelResponse create(ParcelRequest request) {
        String trackingNumber = this.generateTrackingNumber();
        Parcel parcel = MapperService.mapToParcel(request, trackingNumber);
        Parcel saved = parcelRepository.save(parcel);

        return MapperService.mapToParcelResponse(saved);
    }

    @Transactional
    public ParcelResponse update(Long id, ParcelRequest request) {
        Parcel parcel = parcelRepository.findById(id).orElseThrow(() -> new ParcelNotFoundException(PARCEL_NOT_FOUND + id));

        Parcel saved = parcelRepository.save(MapperService.updateParcel(parcel, request));
        return MapperService.mapToParcelResponse(saved);
    }

    public void delete(Long id) {
        if (!parcelRepository.existsById(id)) {
            throw new RuntimeException("Parcel with id " + id + " not found");
        }
        parcelRepository.deleteById(id);
    }

    private String generateTrackingNumber() {
        final String alphabet = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
        SecureRandom rnd = new SecureRandom();

        for (int attempt = 0; attempt < 50; attempt++) {
            StringBuilder sb = new StringBuilder(12);
            for (int i = 0; i < 12; i++) {
                sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
            }
            String candidate = sb.toString();
            if (!parcelRepository.existsByTrackingNumber(candidate)) {
                return candidate;
            }
        }
        return "TRK" + System.currentTimeMillis();
    }

    private String locationLabel(Location l) {
        // направи го “приятно” за dropdown-и/таблица
        return String.format("%s, %s (%s)",
                safe(l.getLocationCountry()),
                safe(l.getLocationRegion()),
                safe(l.getProvince())
        );
    }


    // --------------------------
    // Methods used by ParcelViewController
    // --------------------------

    private String safe(String v) {
        return v == null ? "-" : v;
    }

    @Transactional(readOnly = true)
    public List<Parcel> fetchAllParcels() {
        return parcelRepository.findAllByOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public List<Parcel> fetchParcelsForClient(Long clientId) {
        return parcelRepository.findBySenderUser_IdOrReceiverUser_IdOrderByIdDesc(clientId, clientId);
    }

    @Transactional(readOnly = true)
    public ParcelResponse fetchParcelByID(Long parcelId) {
        Parcel parcel = parcelRepository.findWithAllById(parcelId)
                .orElseThrow(() -> new ParcelNotFoundException(PARCEL_NOT_FOUND + parcelId));


        return MapperService.mapToParcelResponse(parcel);
    }

    @Transactional(readOnly = true)
    public ParcelResponse fetchParcelByTrackingNumber(String trackingNumber) {
        Parcel parcel = parcelRepository.findByTrackingNumberContainingIgnoreCaseOrderByIdDesc(trackingNumber).stream().findFirst()
                .orElseThrow(() -> new RuntimeException(PARCEL_NOT_FOUND + trackingNumber));

        return MapperService.mapToParcelResponse(parcel);
    }

    @Transactional(readOnly = true)
    public List<ParcelResponse> fetchParcelsByStaff(Long staffId) {
        List<Parcel> parcels = parcelRepository.findByStaff_StaffIdOrderByIdDesc(staffId);

        return getParcelResponses(parcels);
    }

    @Transactional(readOnly = true)
    public List<ParcelResponse> fetchAllNotDeliveredParcels() {
        List<Parcel> parcels = parcelRepository.findByReceivedDateIsNullOrderByIdDesc();

        return getParcelResponses(parcels);
    }

    @Transactional(readOnly = true)
    public List<ParcelResponse> fetchParcelsBySender(Long senderId) {
        List<Parcel> parcels = parcelRepository.findBySenderUser_IdOrderByIdDesc(senderId);

        return getParcelResponses(parcels);
    }

    @Transactional(readOnly = true)
    public List<ParcelResponse> fetchParcelsByReceiver(Long receiverId) {
        List<Parcel> parcels = parcelRepository.findByReceiverUser_IdOrderByIdDesc(receiverId);

        return getParcelResponses(parcels);
    }

    @Transactional
    public String createParcel(ParcelRequest request) {
        String trackingNumber = this.generateTrackingNumber();
        Parcel parcel = MapperService.mapToParcel(request, trackingNumber);

        // Set sender user
        UserDetails senderUser = findOrCreateUser(
                request.senderFirstName(),
                request.senderLastName(),
                request.senderPhone()
        );
        parcel.setSenderUser(senderUser);

        // Set receiver user
        UserDetails receiverUser = findOrCreateUser(
                request.recipientFirstName(),
                request.recipientLastName(),
                request.recipientPhone()
        );
        parcel.setReceiverUser(receiverUser);

        // Set staff
        if (isNotEmpty(request.staffName())) {
            Staff staff = findStaffByName(request.staffName());
            parcel.setStaff(staff);
        }

        // Set send location (find existing by region/country)
        if (parcel.getSendLocation() != null) {
            Location sendLocation = findOrCreateLocation(parcel.getSendLocation());
            parcel.setSendLocation(sendLocation);
        }

        // Set receiver location (find existing or create new)
        if (parcel.getReceiverLocation() != null) {
            Location receiverLocation = findOrCreateLocation(parcel.getReceiverLocation());
            parcel.setReceiverLocation(receiverLocation);
        }

        // Set price location tax based on delivery type (office vs address)
        // If receiverLocation has a value, it's office delivery (ID 1), otherwise address (ID 2)
        String deliveryType = isNotEmpty(request.receiverLocation()) ? "office" : "address";
        PriceLocationTax locationTax = findPriceLocationTaxByDeliveryType(deliveryType);
        parcel.setPriceLocationTax(locationTax);

        // Set price weight tax based on parcel weight
        if (request.weight() != null) {
            PriceWeightTax weightTax = findPriceWeightTax(request.weight());
            parcel.setPriceWeightTax(weightTax);
        }

        // Calculate and set price
        BigDecimal price = calculatePrice(parcel);
        parcel.setPrice(price);

        // Set sent date to now
        parcel.setSentDate(LocalDateTime.now());

        parcelRepository.save(parcel);
        return trackingNumber;
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private UserDetails findOrCreateUser(String firstName, String lastName, String phone) {
        // Try to find existing user by phone number
        List<UserDetails> allUsers = userDetailsRepository.findAll();
        for (UserDetails user : allUsers) {
            if (phone != null && phone.equals(user.getPhoneNumber())) {
                return user;
            }
        }

        // Create new user
        UserDetails newUser = new UserDetails();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setPhoneNumber(phone);
        return userDetailsRepository.save(newUser);
    }

    private Staff findStaffByName(String staffName) {
        String[] parts = staffName.split(" ", 2);
        String firstName = parts.length > 0 ? parts[0].trim() : "";
        String lastName = parts.length > 1 ? parts[1].trim() : "";

        List<Staff> staffList = staffRepository.findAll();
        for (Staff staff : staffList) {
            UserDetails userDetails = staff.getStaffUserDetails();
            if (userDetails != null) {
                boolean firstNameMatch = firstName.equalsIgnoreCase(userDetails.getFirstName());
                boolean lastNameMatch = lastName.equalsIgnoreCase(userDetails.getLastName());
                if (firstNameMatch && lastNameMatch) {
                    return staff;
                }
            }
        }
        return null;
    }

    private Location findOrCreateLocation(Location location) {
        // Try to find existing location
        List<Location> locations = locationRepository.findAll();
        for (Location loc : locations) {
            boolean regionMatch = (location.getLocationRegion() == null && loc.getLocationRegion() == null) ||
                    (location.getLocationRegion() != null && location.getLocationRegion().equalsIgnoreCase(loc.getLocationRegion()));
            boolean countryMatch = (location.getLocationCountry() == null && loc.getLocationCountry() == null) ||
                    (location.getLocationCountry() != null && location.getLocationCountry().equalsIgnoreCase(loc.getLocationCountry()));

            if (regionMatch && countryMatch) {
                return loc;
            }
        }

        // Save new location
        return locationRepository.save(location);
    }

    private PriceWeightTax findPriceWeightTax(Double weight) {
        List<PriceWeightTax> taxes = priceWeightTaxRepository.findAll();
        // Find the tax where weight <= maxWeightAmount, sorted by maxWeightAmount ascending
        return taxes.stream()
                .filter(t -> t.getMaxWeightAmount() != null && weight <= t.getMaxWeightAmount())
                .min(Comparator.comparing(PriceWeightTax::getMaxWeightAmount))
                .orElse(taxes.stream()
                        .max(Comparator.comparing(t -> t.getMaxWeightAmount() != null ? t.getMaxWeightAmount() : 0.0))
                        .orElse(null));
    }

    private PriceLocationTax findPriceLocationTax(Location location) {
        List<PriceLocationTax> taxes = priceLocationTaxRepository.findAll();
        for (PriceLocationTax tax : taxes) {
            if (tax.getLocation() != null && tax.getLocation().getLocationId().equals(location.getLocationId())) {
                return tax;
            }
        }
        return null;
    }

    /**
     * Finds location tax by delivery type.
     * Uses hardcoded IDs: 1 for office delivery, 2 for address delivery
     */
    public PriceLocationTax findPriceLocationTaxByDeliveryType(String deliveryType) {
        Long taxId = "office".equalsIgnoreCase(deliveryType) ? 1L : 2L;
        return priceLocationTaxRepository.findById(taxId).orElse(null);
    }

    /**
     * Calculates price estimate for display without creating a parcel.
     */
    @Transactional(readOnly = true)
    public PriceCalculationResponse calculatePriceEstimate(Double weight, String deliveryType) {
        BigDecimal weightTaxAmount = BigDecimal.ZERO;
        BigDecimal locationTaxAmount = BigDecimal.ZERO;

        // Get weight tax
        if (weight != null && weight > 0) {
            PriceWeightTax weightTax = findPriceWeightTax(weight);
            if (weightTax != null && weightTax.getWeightTax() != null) {
                weightTaxAmount = weightTax.getWeightTax();
            }
        }

        // Get location tax based on delivery type (ID 1 = office, ID 2 = address)
        if (deliveryType != null && !deliveryType.isEmpty()) {
            PriceLocationTax locationTax = findPriceLocationTaxByDeliveryType(deliveryType);
            if (locationTax != null && locationTax.getLocationTax() != null) {
                locationTaxAmount = locationTax.getLocationTax();
            }
        }

        BigDecimal totalPrice = weightTaxAmount.add(locationTaxAmount);

        return new PriceCalculationResponse(weightTaxAmount, locationTaxAmount, totalPrice);
    }

    private BigDecimal calculatePrice(Parcel parcel) {
        BigDecimal weightTax = BigDecimal.ZERO;
        BigDecimal locationTax = BigDecimal.ZERO;

        if (parcel.getPriceWeightTax() != null && parcel.getPriceWeightTax().getWeightTax() != null) {
            weightTax = parcel.getPriceWeightTax().getWeightTax();
        }

        if (parcel.getPriceLocationTax() != null && parcel.getPriceLocationTax().getLocationTax() != null) {
            locationTax = parcel.getPriceLocationTax().getLocationTax();
        }

        return weightTax.add(locationTax);
    }

    @Transactional(readOnly = true)
    public ParcelResponse fetchParcelResponseById(Long id) {
        Parcel parcel = parcelRepository.findWithAllById(id)
                .orElseThrow(() -> new RuntimeException("Parcel with id " + id + " not found"));

        return MapperService.mapToParcelResponse(parcel);
    }

    public List<ParcelAdminRow> fetchAllAdminParcels(String q, boolean hideDelivered) {
        String query = (q == null) ? "" : q.trim();

        return parcelRepository.adminSearch(query, hideDelivered)
                .stream()
                .map(this::toAdminRow)
                .toList();
    }

    private String fullName(UserDetails u) {
        if (u == null) return null;
        return String.join(" ",
                safe(u.getFirstName()),
                safe(u.getMiddleName()),
                safe(u.getLastName())
        ).trim().replaceAll("\\s+", " ");
    }


    private ParcelAdminRow toAdminRow(Parcel p) {
        String senderName = fullName(p.getSenderUser());
        String receiverName = fullName(p.getReceiverUser());

        String staffName = null;
        if (p.getStaff() != null) {
            // според твоя модел (ти каза getStaffUserDetails())
            staffName = fullName(p.getStaff().getStaffUserDetails());
        }

        String sendLocationLabel = locationLabel(p.getSendLocation());
        String receiverLocationLabel = locationLabel(p.getReceiverLocation());

        return new ParcelAdminRow(
                p.getId(),
                p.getTrackingNumber(),
                p.getWeight(),
                p.getPrice(),
                p.getSentDate(),
                p.getReceivedDate(),
                p.getParcelStatus(),
                sendLocationLabel,
                receiverLocationLabel,
                p.getSenderUser() != null ? p.getSenderUser().getId() : null,
                p.getReceiverUser() != null ? p.getReceiverUser().getId() : null,
                p.getStaff() != null ? p.getStaff().getStaffId() : null,
                senderName,
                receiverName,
                staffName
        );
    }

    public RevenueReport revenueReport(LocalDate from, LocalDate to) {

        // inclusive период: [from 00:00, to+1 00:00)
        LocalDateTime fromDt = from.atStartOfDay();
        LocalDateTime toDt = to.plusDays(1).atStartOfDay();

        List<Parcel> parcels = parcelRepository.findAllByReceivedDateBetweenAndParcelStatus(
                fromDt, toDt, ParcelStatus.DELIVERED
        );

        BigDecimal baseSum = BigDecimal.ZERO;
        BigDecimal weightSum = BigDecimal.ZERO;
        BigDecimal locationSum = BigDecimal.ZERO;

        List<RevenueRow> rows = parcels.stream().map(p -> {
            BigDecimal base = nz(p.getPrice());
            BigDecimal wTax = (p.getPriceWeightTax() != null) ? nz(p.getPriceWeightTax().getWeightTax()) : BigDecimal.ZERO;
            BigDecimal lTax = (p.getPriceLocationTax() != null) ? nz(p.getPriceLocationTax().getLocationTax()) : BigDecimal.ZERO;

            BigDecimal total = base.add(wTax).add(lTax);

            return new RevenueRow(
                    p.getId(),
                    p.getTrackingNumber(),
                    p.getSentDate(),
                    p.getReceivedDate(),
                    p.getParcelStatus() != null ? p.getParcelStatus().name() : null,
                    base,
                    wTax,
                    lTax,
                    total
            );
        }).toList();

        for (RevenueRow r : rows) {
            baseSum = baseSum.add(nz(r.basePrice()));
            weightSum = weightSum.add(nz(r.weightTax()));
            locationSum = locationSum.add(nz(r.locationTax()));
        }

        BigDecimal totalRevenue = baseSum.add(weightSum).add(locationSum);

        return new RevenueReport(
                from,
                to,
                rows.size(),
                baseSum,
                weightSum,
                locationSum,
                totalRevenue,
                rows
        );
    }

    private BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    public ParcelResponse getParcelFormWithDefaults() {

        List<String> staffNames = this.dropdownService.createStaffDropdown();
        List<String> officeLocations = this.dropdownService.cerateOfficeLocationDropdown();

        return new ParcelResponse(null, null, null, null, null, null, null,
                null, null, officeLocations, null, null, null, null,
                null, null, officeLocations, null, null,
                null, null, staffNames);
    }
}
