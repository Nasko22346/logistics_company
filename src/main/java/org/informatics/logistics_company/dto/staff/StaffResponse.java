package org.informatics.logistics_company.dto.staff;

import org.informatics.logistics_company.dto.templates.ObjectResponse;
import org.informatics.logistics_company.model.enums.Position;
import org.informatics.logistics_company.model.jpa.UserInfo;

public record StaffResponse(
        Long staffId,
        Long officeId,
        Position position,
        UserInfo staffInfo
) implements ObjectResponse {}
