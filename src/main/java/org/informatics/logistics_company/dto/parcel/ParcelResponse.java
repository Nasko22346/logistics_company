package org.informatics.logistics_company.dto.parcel;

import lombok.Data;
import org.informatics.logistics_company.model.jpa.Parcel;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ParcelResponse {
    private Long id;
    private Double weight;
    private BigDecimal price;
    private String status;
    private LocalDate sentDate;
    private LocalDate receivedDate;
    private String senderName;
    private String receiverName;
    private String registeredByStaffName;

    public ParcelResponse(Parcel parcel) {
        this.id = parcel.getId();
        this.weight = parcel.getWeight();
        this.price = parcel.getPrice();
        this.status = parcel.getParcelStatus().name();
        this.sentDate = parcel.getSentDate() != null ? parcel.getSentDate().toLocalDate() : null;
        this.receivedDate = parcel.getReceivedDate() != null ? parcel.getReceivedDate().toLocalDate() : null;
        this.senderName = parcel.getSenderUser() != null ? parcel.getSenderUser().getFirstName() + " " + parcel.getSenderUser().getLastName() : null;
        this.receiverName = parcel.getReceiverUser() != null ? parcel.getReceiverUser().getFirstName() + " " + parcel.getReceiverUser().getLastName() : null;
        this.registeredByStaffName = parcel.getStaff() != null && parcel.getStaff().getStaffUserInfo() != null ?
            parcel.getStaff().getStaffUserInfo().getFirstName() + " " +
            parcel.getStaff().getStaffUserInfo().getLastName() : null;
    }
}
