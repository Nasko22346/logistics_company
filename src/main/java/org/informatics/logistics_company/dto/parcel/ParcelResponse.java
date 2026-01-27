package org.informatics.logistics_company.dto.parcel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ParcelResponse(
        // Parcel information
        Long id,
        Double weight,
        LocalDateTime sentDate,
        LocalDateTime receivedDate,
        String parcelStatus,
        BigDecimal weightTax,
        BigDecimal locationTax,
        String trackingNumber,
        BigDecimal price,

        // Office information where the parcel was sent from
        List<String> sendLocation, // Concatenate location.region and location.country

        // Sender information
        String senderFirstName,
        String senderLastName,
        String senderPhone,

        // Recipient information
        String recipientFirstName,
        String recipientLastName,
        String recipientPhone,

        // Receiver location (office delivery where the parcel is sent to)
        List<String> receiverLocation, // Concatenate location.region and location.country

        // Address delivery fields
        String locationCountry,
        String province,
        String locationRegion,
        String locationDescription,

        // Staff information
        List<String> staffName // Concatenated staff first and last name
) {}
