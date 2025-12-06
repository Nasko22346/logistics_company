package org.informatics.logistics_company.service;

import org.informatics.logistics_company.dto.parcel.ParcelRequest;
import org.informatics.logistics_company.dto.parcel.ParcelResponse;
import org.informatics.logistics_company.model.enums.ParcelStatus;
import org.informatics.logistics_company.model.jpa.Parcel;
import org.informatics.logistics_company.repository.ParcelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParcelService {

    private final ParcelRepository parcelRepository;

    public ParcelService(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }


    /**
     * Method to create a new parcel
     *
     * @param parcelRequest - data for the new parcel
     * @return created parcel
     */
    // TODO: Check what to return
    public ParcelResponse createParcel(ParcelRequest parcelRequest) {
        Parcel parcel = new Parcel();
        Parcel saved = parcelRepository.save(parcel);
        return null;
    }

    /**
     * Method to update a parcel
     *
     * @param id            - id of the parcel to be updated
     * @param parcelRequest - new data for the parcel
     * @return updated parcel
     */
    public ParcelResponse updateParcel(Long id, ParcelRequest parcelRequest) {
        Parcel parcel = parcelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcel not found"));

        Parcel saved = parcelRepository.save(parcel);
        return null;
    }

    /**
     * Method to delete a parcel
     *
     * @param id - id of the parcel to be deleted
     * @return the id of the deleted records
     */
    public long deleteParcel(Long id) {
        if (!parcelRepository.existsById(id)) {
            //TODO: Implement custom exceptions
            throw new RuntimeException("Parcel not found");
        }

        return parcelRepository.deleteByIdentificationNumber(id);
    }

    /**
     * Method to fetch a parcel by ID
     *
     * @param id - id of the parcel to be fetched
     * @return the parcel with the given id
     */
    public ParcelResponse fetchParcelByID(Long id) {
        return parcelRepository.findById(id)
                .map(ParcelResponse::new)
                .orElseThrow(() -> new RuntimeException("Parcel not found"));
    }


    /**
     * Method to fetch all parcels
     *
     * @return list of all parcels
     */
    public List<ParcelResponse> fetchAllParcels() {
        return parcelRepository.findAll().stream().map(ParcelResponse::new).toList();
    }


    /**
     * Method to fetch all parcels for a given client (both sent and received)
     *
     * @param clientId - id of the client
     * @return list of all parcels for the given client
     */
    public List<ParcelResponse> fetchParcelsForClient(Long clientId) {
        List<ParcelResponse> sent = parcelRepository.findAllBySenderUserId(clientId).stream()
                .map(ParcelResponse::new).collect(Collectors.toList());
        List<ParcelResponse> received = parcelRepository.extractAllParcelsByReceiverID(clientId).stream()
                .map(ParcelResponse::new).toList();
        sent.addAll(received);
        return sent;
    }

    /**
     * Method to fetch all parcels registered by a given staff member
     *
     * @param staffId - id of the staff member
     * @return list of all parcels registered by the given staff member
     */
    public List<ParcelResponse> fetchParcelsByStaff(Long staffId) {
        return parcelRepository.findAllByStaffStaffId(staffId)
                .stream().map(ParcelResponse::new).toList();
    }

    /**
     * Method to fetch all parcels that are not delivered
     *
     * @return list of all parcels that are not delivered
     */
    public List<ParcelResponse> fetchAllNotDeliveredParcels() {
        return parcelRepository.extractAllNotDeliveredParcels(ParcelStatus.DELIVERED)
                .stream().map(ParcelResponse::new).collect(Collectors.toList());
    }
}
