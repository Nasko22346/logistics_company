package org.informatics.logistics_company.dto.parcel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ParcelRequest(
        // Parcel information
        Double weight,
        LocalDateTime sentDate,
        LocalDateTime receivedDate,
        String parcelStatus,
        BigDecimal weightTax,
        BigDecimal locationTax,
        String trackingNumber,

        // Sender information
        String sendLocation,

        // Sender information
        String senderFirstName,
        String senderLastName,
        String senderPhone,

        // Recipient information
        String recipientFirstName,
        String recipientLastName,
        String recipientPhone,

        // Receiver location (office delivery)
        String receiverLocation,

        // Address delivery fields
        String locationCountry,
        String province,
        String locationRegion,
        String locationDescription,

        // Staff information
        String staffName // Concatenated staff first and last name
) {

    public ParcelRequest() {
        this(null, null, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null);
    }
}
