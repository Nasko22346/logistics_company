package org.informatics.logistics_company.dto.parcel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ParcelResponse(Long id, String trackingNumber, Double weight, BigDecimal price, LocalDateTime sentDate,
        LocalDateTime receivedDate, String parcelStatus, String sendLocation, String receiverLocation, String senderName,
        String receiverName, BigDecimal locationTax, BigDecimal weightTax, String staffName
) {}
