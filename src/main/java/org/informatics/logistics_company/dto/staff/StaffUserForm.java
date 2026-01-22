package org.informatics.logistics_company.dto.staff;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.informatics.logistics_company.model.enums.Position;

@Data
public class StaffUserForm {

    private Long staffId;

    @NotNull(message = "Office е задължително")
    private Long officeId;

    @NotNull(message = "Position е задължително")
    private Position position;

    @NotBlank @Size(max = 60)
    private String firstName;

    @Size(max = 60)
    private String middleName;

    @NotBlank @Size(max = 60)
    private String lastName;

    @Size(max = 40)
    private String phoneNumber;

    @NotBlank @Email @Size(max = 120)
    private String email;

    private String password;
}
