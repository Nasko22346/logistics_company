package org.informatics.logistics_company.controller;

import jakarta.validation.Valid;
import org.informatics.logistics_company.dto.parcel.ParcelRequest;
import org.informatics.logistics_company.dto.parcel.ParcelResponse;
import org.informatics.logistics_company.service.ParcelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parcel")
public class ParcelController {

    private final ParcelService parcelService;

    public ParcelController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    /**
     * Method to fetch all parcels
     *
     * @return list of all parcels
     */
    @GetMapping("/all")
    public List<ParcelResponse> getAllParcels() {
        return parcelService.fetchAllParcels();
    }

    /**
     * Method to fetch parcels for a specific client
     *
     * @param clientId - id of the client
     * @return list of parcels for the client
     */
    @GetMapping("/my-packages/{clientId}")
    public List<ParcelResponse> getClientParcels(@PathVariable Long clientId) {
        return parcelService.fetchParcelsForClient(clientId);
    }

    /**
     * Method to fetch parcels assigned to a specific staff member
     *
     * @param staffId - id of the staff member
     * @return list of parcels assigned to the staff member
     */
    @GetMapping("/staff/{staffId}")
    public List<ParcelResponse> getByStaff(@PathVariable Long staffId) {
        return parcelService.fetchParcelsByStaff(staffId);
    }

    /**
     * Method to fetch all not delivered parcels
     *
     * @return list of not delivered parcels
     */
    @GetMapping("/not-delivered")
    public List<ParcelResponse> getNotDelivered() {
        return parcelService.fetchAllNotDeliveredParcels();
    }

    /**
     * Method to fetch a parcel by ID
     *
     * @param id - id of the parcel
     * @return the parcel with the given id
     */
    @GetMapping("/{id}")
    public ResponseEntity<ParcelResponse> getParcelById(@PathVariable Long id) {
        return ResponseEntity.ok(parcelService.fetchParcelByID(id));
    }

    /**
     * Method to create a new parcel
     *
     * @param parcelRequest - data for the new parcel
     * @return created parcel
     */
    @PostMapping("/create")
    public ResponseEntity<ParcelResponse> createParcel(@Valid @RequestBody ParcelRequest parcelRequest) {
        return ResponseEntity.ok(parcelService.createParcel(parcelRequest));
    }

    /**
     * Method to update a parcel
     *
     * @param id            - id of the parcel to be updated
     * @param parcelRequest - new data for the parcel
     * @return updated parcel
     */
    @PutMapping("/{id}")
    public ResponseEntity<ParcelResponse> updateParcel(@PathVariable Long id, @Valid @RequestBody ParcelRequest parcelRequest) {
        return ResponseEntity.ok(parcelService.updateParcel(id, parcelRequest));
    }

    /**
     * Method to delete a parcel
     *
     * @param id - id of the parcel to be deleted
     * @return the id of the deleted records
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteParcel(@PathVariable Long id) {
        return ResponseEntity.ok(parcelService.deleteParcel(id));
    }
}
