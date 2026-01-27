package org.informatics.logistics_company.service;

import lombok.experimental.UtilityClass;
import org.informatics.logistics_company.dto.parcel.ParcelRequest;
import org.informatics.logistics_company.dto.parcel.ParcelResponse;
import org.informatics.logistics_company.model.enums.ParcelStatus;
import org.informatics.logistics_company.model.jpa.Location;
import org.informatics.logistics_company.model.jpa.Parcel;
import org.informatics.logistics_company.model.jpa.Staff;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@UtilityClass
public class MapperService {

    public ParcelResponse mapToParcelResponse(Parcel parcel) {
        return new ParcelResponse(
                parcel.getId(),
                parcel.getWeight(),
                parcel.getSentDate(),
                parcel.getReceivedDate(),
                parcel.getParcelStatus() != null ? parcel.getParcelStatus().getValue() : null,
                parcel.getPriceWeightTax() != null ? parcel.getPriceWeightTax().getWeightTax() : null,
                parcel.getPriceLocationTax() != null ? parcel.getPriceLocationTax().getLocationTax() : null,
                parcel.getTrackingNumber(),
                parcel.getPrice(),
                buildLocationList(parcel.getSendLocation()),
                parcel.getSenderUser() != null ? parcel.getSenderUser().getFirstName() : null,
                parcel.getSenderUser() != null ? parcel.getSenderUser().getLastName() : null,
                parcel.getSenderUser() != null ? parcel.getSenderUser().getPhoneNumber() : null,
                parcel.getReceiverUser() != null ? parcel.getReceiverUser().getFirstName() : null,
                parcel.getReceiverUser() != null ? parcel.getReceiverUser().getLastName() : null,
                parcel.getReceiverUser() != null ? parcel.getReceiverUser().getPhoneNumber() : null,
                buildLocationList(parcel.getReceiverLocation()),
                parcel.getReceiverLocation() != null ? parcel.getReceiverLocation().getLocationCountry() : null,
                parcel.getReceiverLocation() != null ? parcel.getReceiverLocation().getProvince() : null,
                parcel.getReceiverLocation() != null ? parcel.getReceiverLocation().getLocationRegion() : null,
                parcel.getReceiverLocation() != null ? parcel.getReceiverLocation().getLocationDescription() : null,
                buildStaffNameList(parcel.getStaff())
        );
    }

    public Parcel mapToParcel(ParcelRequest request, String trackingNumber) {
        Parcel parcel = new Parcel();
        parcel.setTrackingNumber(trackingNumber);
        setIfNotNull(request.weight(), parcel::setWeight);
        parcel.setSentDate(LocalDateTime.now());
        setIfNotNull(request.receivedDate(), parcel::setReceivedDate);

        parcel.setParcelStatus(ParcelStatus.IN_TRANSIT);

        // Create send location from "Region, Country" format
        if (isNotEmpty(request.sendLocation())) {
            parcel.setSendLocation(createLocationFromString(request.sendLocation()));
        }

        // Create receiver location - either from office selection or manual address fields
        if (isNotEmpty(request.receiverLocation())) {
            // Office delivery - split "Region, Country"
            parcel.setReceiverLocation(createLocationFromString(request.receiverLocation()));
        } else {
            // Address delivery - use individual fields
            parcel.setReceiverLocation(createLocationFromFields(
                    request.locationCountry(),
                    request.province(),
                    request.locationRegion(),
                    request.locationDescription()
            ));
        }

        return parcel;
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private Location createLocationFromString(String locationString) {
        Location location = new Location();
        String[] parts = locationString.split(", ");
        if (parts.length >= 2) {
            location.setLocationRegion(parts[0].trim());
            location.setLocationCountry(parts[1].trim());
        } else if (parts.length == 1) {
            location.setLocationRegion(parts[0].trim());
        }
        return location;
    }

    private Location createLocationFromFields(String country, String province, String region, String description) {
        Location location = new Location();
        location.setLocationCountry(country);
        location.setProvince(province);
        location.setLocationRegion(region);
        location.setLocationDescription(description);
        return location;
    }

    public Parcel updateParcel(Parcel parcel, ParcelRequest request) {
        setIfNotNull(request.weight(), parcel::setWeight);
        setIfNotNull(request.sentDate(), parcel::setSentDate);
        setIfNotNull(request.receivedDate(), parcel::setReceivedDate);
        setIfNotNull(request.trackingNumber(), parcel::setTrackingNumber);
        if (request.parcelStatus() != null) {
            parcel.setParcelStatus(ParcelStatus.valueOf(request.parcelStatus()));
        }

        return parcel;
    }

    private List<String> buildLocationList(Location location) {
        if (location == null) {
            return null;
        }
        return Arrays.asList(location.getLocationRegion(), location.getLocationCountry());
    }

    private List<String> buildStaffNameList(Staff staff) {
        if (staff == null || staff.getStaffUserDetails() == null) {
            return null;
        }
        return Arrays.asList(
                staff.getStaffUserDetails().getFirstName(),
                staff.getStaffUserDetails().getLastName()
        );
    }

    private static <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
