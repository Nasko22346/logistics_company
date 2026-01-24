package org.informatics.logistics_company.dto.parcel;

import org.informatics.logistics_company.model.enums.ParcelStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ParcelAdminRow(
        Long id,
        String trackingNumber,
        Double weight,
        BigDecimal price,
        LocalDateTime sentDate,
        LocalDateTime receivedDate,
        ParcelStatus parcelStatus,

        String sendLocationLabel,
        String receiverLocationLabel,

        Long senderUserId,
        Long receiverUserId,
        Long staffId,

        String senderName,
        String receiverName,
        String staffName
) {}
