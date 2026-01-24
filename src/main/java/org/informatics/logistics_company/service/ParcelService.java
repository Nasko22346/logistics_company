package org.informatics.logistics_company.service;

import org.informatics.logistics_company.dto.parcel.ParcelRequest;
import org.informatics.logistics_company.dto.parcel.ParcelResponse;
import org.informatics.logistics_company.model.jpa.*;
import org.informatics.logistics_company.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParcelService {

    private final ParcelRepository parcelRepository;
    private final LocationRepository locationRepository;
    private final PriceLocationTaxRepository priceLocationTaxRepository;
    private final PriceWeightTaxRepository priceWeightTaxRepository;
    private final StaffRepository staffRepository;
    private final UserDetailsRepository userDetailsRepository;

    public ParcelService(
            ParcelRepository parcelRepository,
            LocationRepository locationRepository,
            PriceLocationTaxRepository priceLocationTaxRepository,
            PriceWeightTaxRepository priceWeightTaxRepository,
            StaffRepository staffRepository,
            UserDetailsRepository userDetailsRepository
    ) {
        this.parcelRepository = parcelRepository;
        this.locationRepository = locationRepository;
        this.priceLocationTaxRepository = priceLocationTaxRepository;
        this.priceWeightTaxRepository = priceWeightTaxRepository;
        this.staffRepository = staffRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    public List<ParcelResponse> listAll(String q) {
        var list = (q != null && !q.isBlank())
                ? parcelRepository.findByTrackingNumberContainingIgnoreCaseOrderByIdDesc(q.trim())
                : parcelRepository.findAllByOrderByIdDesc();

        return list.stream().map(this::toResponse).toList();
    }

    public ParcelResponse getById(Long id) {
        Parcel p = parcelRepository.findWithAllById(id)
                .orElseThrow(() -> new RuntimeException("Parcel with id " + id + " not found"));
        return toResponse(p);
    }

    public ParcelResponse create(ParcelRequest r) {
        Parcel p = new Parcel();

        // tracking number - auto
        p.setTrackingNumber(generateTrackingNumber());

        applyRequest(p, r);

        Parcel saved = parcelRepository.save(p);
        return toResponse(saved);
    }

    public ParcelResponse update(Long id, ParcelRequest r) {
        Parcel p = parcelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcel with id " + id + " not found"));

        // ако имаш стари записи без tracking:
        if (p.getTrackingNumber() == null || p.getTrackingNumber().isBlank()) {
            p.setTrackingNumber(generateTrackingNumber());
        }

        applyRequest(p, r);

        Parcel saved = parcelRepository.save(p);
        return toResponse(saved);
    }

    public void delete(Long id) {
        if (!parcelRepository.existsById(id)) {
            throw new RuntimeException("Parcel with id " + id + " not found");
        }
        parcelRepository.deleteById(id);
    }

    private void applyRequest(Parcel p, ParcelRequest r) {
        p.setWeight(r.weight());
        p.setPrice(r.price());
        p.setSentDate(r.sentDate());
        p.setReceivedDate(r.receivedDate());
        p.setParcelStatus(r.parcelStatus());

        p.setSendLocation(fetchLocation(r.sendLocationId()));
        p.setReceiverLocation(fetchLocation(r.receiverLocationId()));

        p.setSenderUser(fetchUser(r.senderUserId()));
        p.setReceiverUser(fetchUser(r.receiverUserId()));

        p.setPriceLocationTax(fetchLocationTax(r.priceLocationTaxId()));
        p.setPriceWeightTax(fetchWeightTax(r.priceWeightTaxId()));

        p.setStaff(fetchStaff(r.staffId()));
    }

    private Location fetchLocation(Long id) {
        if (id == null) return null;
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location with id " + id + " not found"));
    }

    private PriceLocationTax fetchLocationTax(Long id) {
        if (id == null) return null;
        return priceLocationTaxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PriceLocationTax with id " + id + " not found"));
    }

    private PriceWeightTax fetchWeightTax(Long id) {
        if (id == null) return null;
        return priceWeightTaxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PriceWeightTax with id " + id + " not found"));
    }

    private Staff fetchStaff(Long id) {
        if (id == null) return null;
        return staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff with id " + id + " not found"));
    }

    private UserDetails fetchUser(Long id) {
        if (id == null) return null;
        return userDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
    }

    private ParcelResponse toResponse(Parcel p) {
        return new ParcelResponse(
                p.getId(),
                p.getTrackingNumber(),
                p.getWeight(),
                p.getPrice(),
                p.getSentDate(),
                p.getReceivedDate(),
                p.getParcelStatus(),

                p.getSendLocation() != null ? p.getSendLocation().getLocationId() : null,
                p.getSendLocation() != null ? locationLabel(p.getSendLocation()) : null,

                p.getReceiverLocation() != null ? p.getReceiverLocation().getLocationId() : null,
                p.getReceiverLocation() != null ? locationLabel(p.getReceiverLocation()) : null,

                p.getSenderUser() != null ? p.getSenderUser().getId() : null,
                p.getSenderUser() != null ? userLabel(p.getSenderUser()) : null,

                p.getReceiverUser() != null ? p.getReceiverUser().getId() : null,
                p.getReceiverUser() != null ? userLabel(p.getReceiverUser()) : null,

                p.getPriceLocationTax() != null ? p.getPriceLocationTax().getId() : null,
                p.getPriceLocationTax() != null ? p.getPriceLocationTax().getLocationTax() : null,

                p.getPriceWeightTax() != null ? p.getPriceWeightTax().getId() : null,
                p.getPriceWeightTax() != null ? p.getPriceWeightTax().getWeightTax() : null,

                p.getStaff() != null ? p.getStaff().getStaffId() : null,
                p.getStaff() != null ? staffLabel(p.getStaff()) : null
        );
    }

    private String locationLabel(Location l) {
        // направи го “приятно” за dropdown-и/таблица
        return String.format("%s, %s (%s)",
                safe(l.getLocationCountry()),
                safe(l.getLocationRegion()),
                safe(l.getProvince())
        );
    }

    private String userLabel(UserDetails u) {
        // адаптирай според полетата ти (ако имаш first/last)
        return "User #" + u.getId();
    }

    private String staffLabel(Staff s) {
        return "Staff #" + s.getStaffId() + (s.getPosition() != null ? " (" + s.getPosition() + ")" : "");
    }

    private String safe(String v) {
        return v == null ? "-" : v;
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

    // --------------------------
// Methods used by ParcelViewController
// --------------------------

    @Transactional(readOnly = true)
    public List<Parcel> fetchAllParcels() {
        return parcelRepository.findAllByOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public List<Parcel> fetchParcelsForClient(Long clientId) {
        return parcelRepository.findBySenderUser_IdOrReceiverUser_IdOrderByIdDesc(clientId, clientId);
    }

    @Transactional(readOnly = true)
    public Parcel fetchParcelByID(Long parcelId) {
        return parcelRepository.findWithAllById(parcelId)
                .orElseThrow(() -> new RuntimeException("Parcel with id " + parcelId + " not found"));
    }

    @Transactional(readOnly = true)
    public Parcel fetchParcelByTrackingNumber(String trackingNumber) {
        return parcelRepository.findByTrackingNumberContainingIgnoreCaseOrderByIdDesc(trackingNumber).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Parcel with TrackingNumber " + trackingNumber + " not found"));
    }

    @Transactional(readOnly = true)
    public List<Parcel> fetchParcelsByStaff(Long staffId) {
        return parcelRepository.findByStaff_StaffIdOrderByIdDesc(staffId);
    }

    @Transactional(readOnly = true)
    public List<Parcel> fetchAllNotDeliveredParcels() {
        // най-стабилното без да знаем enum стойностите:
        return parcelRepository.findByReceivedDateIsNullOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public List<Parcel> fetchParcelsBySender(Long senderId) {
        return parcelRepository.findBySenderUser_IdOrderByIdDesc(senderId);
    }

    @Transactional(readOnly = true)
    public List<Parcel> fetchParcelsByReceiver(Long receiverId) {
        return parcelRepository.findByReceiverUser_IdOrderByIdDesc(receiverId);
    }

    @Transactional
    public Parcel createParcel(ParcelRequest request) {
        // ⚠️ Тук ползвам най-логичните имена на полета.
        // Ако твоят ParcelRequest е с различни имена – просто ги пренасочи.

        Parcel p = new Parcel();

        p.setWeight(request.weight());
        p.setSentDate(LocalDateTime.now());
        p.setReceivedDate(null);

        // статус – ако request има parcelStatus:
        // p.setParcelStatus(request.parcelStatus());
        // иначе можеш да сложиш дефолт:
        // p.setParcelStatus(ParcelStatus.CREATED);

        p.setSendLocation(locationRepository.findById(request.sendLocationId())
                .orElseThrow(() -> new RuntimeException("Send location not found: " + request.sendLocationId())));

        p.setReceiverLocation(locationRepository.findById(request.receiverLocationId())
                .orElseThrow(() -> new RuntimeException("Receiver location not found: " + request.receiverLocationId())));

        p.setSenderUser(userDetailsRepository.findById(request.senderUserId())
                .orElseThrow(() -> new RuntimeException("Sender user not found: " + request.senderUserId())));

        p.setReceiverUser(userDetailsRepository.findById(request.receiverUserId())
                .orElseThrow(() -> new RuntimeException("Receiver user not found: " + request.receiverUserId())));

        // staff - ако request има staffId (ако нямаш, махни това):
        if (request.staffId() != null) {
            p.setStaff(staffRepository.findById(request.staffId())
                    .orElseThrow(() -> new RuntimeException("Staff not found: " + request.staffId())));
        }

        // tracking number (ако вече си добавил поле в entity-то)
        // p.setTrackingNumber(generateTrackingNumber());

        // taxes (ако искаш да ги сетнеш автоматично):
        // p.setPriceLocationTax(resolveLocationTax(p.getSendLocation(), p.getReceiverLocation()));
        // p.setPriceWeightTax(resolveWeightTax(p.getWeight()));

        return parcelRepository.save(p);
    }

    @Transactional(readOnly = true)
    public ParcelResponse fetchParcelResponseById(Long id) {
        Parcel parcel = parcelRepository.findWithAllById(id)
                .orElseThrow(() -> new RuntimeException("Parcel with id " + id + " not found"));
        return toResponse(parcel);
    }

}
