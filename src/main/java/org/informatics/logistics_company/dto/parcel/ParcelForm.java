package org.informatics.logistics_company.dto.parcel;

import lombok.Data;
import org.informatics.logistics_company.model.enums.ParcelStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ParcelForm {
    private Long id;

    private String trackingNumber;

    private Double weight;
    private BigDecimal price;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime sentDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime receivedDate;

    private Long sendLocationId;
    private Long receiverLocationId;

    private ParcelStatus parcelStatus;

    private Long senderUserId;
    private Long receiverUserId;

    private Long priceLocationTaxId;
    private Long priceWeightTaxId;

    private Long staffId;
}
