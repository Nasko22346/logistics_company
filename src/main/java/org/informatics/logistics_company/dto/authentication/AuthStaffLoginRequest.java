package org.informatics.logistics_company.dto.authentication;

import jakarta.validation.constraints.Size;
import org.informatics.logistics_company.dto.templates.ObjectRequest;

public record AuthStaffLoginRequest(
        @Size(max = 120)
        String email,
        @Size(max = 256)
        String password
) implements ObjectRequest {}
