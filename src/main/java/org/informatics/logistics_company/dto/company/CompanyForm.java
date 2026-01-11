package org.informatics.logistics_company.dto.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyForm {
    private Long id;
    private String companyName;
    private String phoneNumber;
    private String email;
    private String companyEik;
    private String companyDescription;
}
