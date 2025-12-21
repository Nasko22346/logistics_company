package org.informatics.logistics_company.dto.staff;

import jakarta.validation.constraints.NotBlank;
import org.informatics.logistics_company.dto.templates.ObjectRequest;
import org.informatics.logistics_company.model.enums.Position;
import org.informatics.logistics_company.model.jpa.UserInfo;

public record StaffRequest(
        Position position,
        Long officeId,
        UserInfo staffInfo
) implements ObjectRequest {}