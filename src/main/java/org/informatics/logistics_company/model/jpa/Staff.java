package org.informatics.logistics_company.model.jpa;

import jakarta.persistence.*;
import lombok.Data;
import org.informatics.logistics_company.model.enums.Position;

@Data
@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Long staffId;

    @ManyToOne
    @JoinColumn(name = "office_id")
    private Office office;

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private Position position;

    @OneToOne
    @JoinColumn(name = "staff_data_id")
    private UserDetails staffUserDetails;
}
