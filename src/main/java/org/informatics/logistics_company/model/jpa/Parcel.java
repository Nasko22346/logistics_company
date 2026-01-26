package org.informatics.logistics_company.model.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.informatics.logistics_company.model.enums.ParcelStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table(name = "parcel")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parcel_id")
    private Long id;

    @Column(name = "parcel_weight")
    private Double weight;

    @Column(name = "parcel_price")
    private BigDecimal price;

    @Column(name = "parcel_sent_date")
    private LocalDateTime sentDate;

    @Column(name = "parcel_received_date")
    private LocalDateTime receivedDate;

    @ManyToOne
    @JoinColumn(name = "send_location_id")
    private Location sendLocation;

    @ManyToOne
    @JoinColumn(name = "receiver_location_id")
    private Location receiverLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "parcel_status")
    private ParcelStatus parcelStatus;

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private UserDetails senderUser;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id")
    private UserDetails receiverUser;

    @Column(name = "tracking_number", unique = true, length = 32)
    private String trackingNumber;

    @ManyToOne
    @JoinColumn(name = "price_location_tax_id")
    private PriceLocationTax priceLocationTax;

    @ManyToOne
    @JoinColumn(name = "price_weight_id")
    private PriceWeightTax priceWeightTax;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;
}
