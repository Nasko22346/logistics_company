package org.informatics.logistics_company.dto.parcel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ParcelRequest {

    @NotNull(message = "Weight cannot be null")
    @Positive(message = "Weight must be positive")
    private Double weight;

    @NotNull(message = "SenderID cannot be null")
    @Positive(message = "SenderID must be positive")
    private Long senderId;

    @NotNull(message = "ReceiverID cannot be null")
    @Positive(message = "ReceiverID must be positive")
    private Long receiverId;

    @NotNull(message = "RegisteredByStaffID cannot be null")
    @Positive(message = "RegisteredByStaffID must be positive")
    private Long registeredByStaffId;

    @NotNull(message = "SentLocationID cannot be null")
    @Positive(message = "SentLocationID must be positive")
    private Long sentLocationId;

    @NotNull(message = "ReceivedLocationID cannot be null")
    @Positive(message = "ReceivedLocationID must be positive")
    private Long receivedLocationId;


    private boolean deliverToAddress;
}
