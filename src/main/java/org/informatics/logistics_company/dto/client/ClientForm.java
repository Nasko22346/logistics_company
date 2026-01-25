package org.informatics.logistics_company.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.informatics.logistics_company.model.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientForm {
    private Long id;

    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;

    // optional login
    private String loginEmail;
    private String loginPassword;
    private Role role;
}
