package org.informatics.logistics_company.dto.parcel;

import org.informatics.logistics_company.model.enums.ParcelStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ParcelRequest(
        Double weight,
        BigDecimal price,
        LocalDateTime sentDate,
        LocalDateTime receivedDate,
        Long sendLocationId,
        Long receiverLocationId,
        ParcelStatus parcelStatus,
        Long senderUserId,
        Long receiverUserId,
        Long priceLocationTaxId,
        Long priceWeightTaxId,
        Long staffId
) {
    public ParcelRequest() {
        this(null, null, null, null, null, null, null, null, null, null, null, null);
    }
}
