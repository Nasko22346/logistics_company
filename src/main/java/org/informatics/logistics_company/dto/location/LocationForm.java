package org.informatics.logistics_company.dto.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationForm {
    private Long id;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must be at most 100 characters")
    private String locationCountry;

    @Size(max = 100, message = "Province must be at most 100 characters")
    private String province;

    @Size(max = 100, message = "Region must be at most 100 characters")
    private String locationRegion;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String locationDescription;
}
