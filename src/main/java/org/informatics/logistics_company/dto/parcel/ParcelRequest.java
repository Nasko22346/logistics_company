package org.informatics.logistics_company.dto.parcel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ParcelRequest(Double weight, BigDecimal price, LocalDateTime sentDate, LocalDateTime receivedDate,
                            String parcelStatus, String sendLocation, String receiverLocation, String senderFirstName,
                            String senderLastName, String receiverFirstName, String receiverLastName,
                            BigDecimal locationTax, BigDecimal weightTax, String staffName, String trackingNumber
) {

    public ParcelRequest() {
        this(null, null, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null);
    }
}
