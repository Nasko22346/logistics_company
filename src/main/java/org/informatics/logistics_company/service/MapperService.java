package org.informatics.logistics_company.service;

import lombok.experimental.UtilityClass;
import org.informatics.logistics_company.dto.parcel.ParcelRequest;
import org.informatics.logistics_company.dto.parcel.ParcelResponse;
import org.informatics.logistics_company.model.enums.ParcelStatus;
import org.informatics.logistics_company.model.jpa.Parcel;

import java.util.function.Consumer;

@UtilityClass
public class MapperService {

    public ParcelResponse mapToParcelResponse(Parcel parcel) {
        return new ParcelResponse(parcel.getId(), parcel.getTrackingNumber(), parcel.getWeight(), parcel.getPrice(),
                parcel.getSentDate(), parcel.getReceivedDate(), parcel.getParcelStatus().getValue(),
                concatData(parcel.getSendLocation().getLocationRegion(), parcel.getSendLocation().getLocationCountry()),
                concatData(parcel.getReceiverLocation().getLocationRegion(), parcel.getReceiverLocation().getLocationCountry()),
                concatData(parcel.getSenderUser().getFirstName(), parcel.getSenderUser().getLastName()),
                concatData(parcel.getReceiverUser().getFirstName(), parcel.getReceiverUser().getLastName()),
                parcel.getPriceLocationTax().getLocationTax(), parcel.getPriceWeightTax().getWeightTax(),
                concatData(parcel.getStaff().getStaffUserDetails().getFirstName(), parcel.getStaff().getStaffUserDetails().getLastName()));
    }

    public Parcel mapToParcel(ParcelRequest request, String trackingNumber) {
        Parcel parcel = new Parcel();
        parcel.setTrackingNumber(trackingNumber);
        setIfNotNull(request.weight(), parcel::setWeight);
        setIfNotNull(request.price(), parcel::setPrice);
        setIfNotNull(request.sentDate(), parcel::setSentDate);
        setIfNotNull(request.receivedDate(), parcel::setReceivedDate);
        setIfNotNull(request.parcelStatus() != null ? Enum.valueOf(ParcelStatus.class, request.parcelStatus()) : null, parcel::setParcelStatus);

        return parcel;

    }

    public Parcel updateParcel(Parcel parcel, ParcelRequest request) {
        setIfNotNull(request.weight(), parcel::setWeight);
        setIfNotNull(request.sentDate(), parcel::setSentDate);
        setIfNotNull(request.price(), parcel::setPrice);
        setIfNotNull(request.receivedDate(), parcel::setReceivedDate);
        setIfNotNull(request.trackingNumber(), parcel::setTrackingNumber);
        setIfNotNull(request.parcelStatus() != null ? Enum.valueOf(ParcelStatus.class, request.parcelStatus()) : null, parcel::setParcelStatus);

        return parcel;
    }

    private String concatData(String first, String second) {
        return first + " " + second;
    }

    private static <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
