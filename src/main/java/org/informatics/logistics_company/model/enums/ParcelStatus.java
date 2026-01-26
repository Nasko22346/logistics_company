package org.informatics.logistics_company.model.enums;

import lombok.Getter;

@Getter
public enum ParcelStatus {
    PENDING("pending"),
    IN_TRANSIT("in_transit"),
    DELIVERED("delivered"),
    RETURNED("returned"),
    CANCELLED("cancelled");


    private final String value;
    ParcelStatus(String value) {
        this.value = value;
    }
}
