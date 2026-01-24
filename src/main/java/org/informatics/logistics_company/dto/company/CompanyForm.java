package org.informatics.logistics_company.dto.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyForm {
    private Long id;

    @NotBlank
    @Size(max = 120)
    private String companyName;

    @Size(max = 40)
    private String phoneNumber;

    @Email
    @Size(max = 160)
    private String email;

    @NotBlank
    @Size(max = 20)
    private String companyEik;

    @Size(max = 255)
    private String companyDescription;
}
