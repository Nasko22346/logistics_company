package org.informatics.logistics_company.dto.staff;

import org.informatics.logistics_company.dto.templates.ObjectRequest;
import org.informatics.logistics_company.model.enums.Position;
import org.informatics.logistics_company.model.jpa.UserDetails;

public record StaffRequest(
        Position position,
        Long officeId,
        UserDetails staffInfo
) implements ObjectRequest {}