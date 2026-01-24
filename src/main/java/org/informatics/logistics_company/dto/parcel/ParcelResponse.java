package org.informatics.logistics_company.dto.parcel;

import org.informatics.logistics_company.model.enums.ParcelStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ParcelResponse(
        Long id,
        String trackingNumber,
        Double weight,
        BigDecimal price,
        LocalDateTime sentDate,
        LocalDateTime receivedDate,
        ParcelStatus parcelStatus,

        Long sendLocationId,
        String sendLocationLabel,

        Long receiverLocationId,
        String receiverLocationLabel,

        Long senderUserId,
        String senderUserLabel,

        Long receiverUserId,
        String receiverUserLabel,

        Long priceLocationTaxId,
        BigDecimal locationTax,

        Long priceWeightTaxId,
        BigDecimal weightTax,

        Long staffId,
        String staffLabel
) {}
