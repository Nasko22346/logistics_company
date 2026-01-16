package org.informatics.logistics_company.dto.office;

import lombok.Getter;
import lombok.Setter;

public class OfficeForm {
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String officePhone;
    @Getter
    @Setter
    private String officeEmail;
    @Getter
    @Setter
    private Long companyId;
    @Getter
    @Setter
    private Long openTimeId;
    @Getter
    @Setter
    private Long locationId;

    public OfficeForm() {}

    public OfficeForm(Long id, String officePhone, String officeEmail, Long companyId, Long openTimeId, Long locationId) {
        this.id = id;
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
        this.companyId = companyId;
        this.openTimeId = openTimeId;
        this.locationId = locationId;
    }

//    public String getOfficePhone() { return officePhone; }
//    public void setOfficePhone(String officePhone) { this.officePhone = officePhone; }
//
//    public String getOfficeEmail() { return officeEmail; }
//    public void setOfficeEmail(String officeEmail) { this.officeEmail = officeEmail; }
//
//    public Long getCompanyId() { return companyId; }
//    public void setCompanyId(Long companyId) { this.companyId = companyId; }

}
