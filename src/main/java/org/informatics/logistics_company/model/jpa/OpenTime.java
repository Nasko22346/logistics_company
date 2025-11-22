package org.informatics.logistics_company.model.jpa;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;

@Entity
@Table(name = "open_time")
@Data
public class OpenTime {
    @Id
    @Column(name = "work_time_id")
    private Long workTimeId;

    @Column(name = "work_time_day_of_week")
    private Integer workTimeDayOfWeek;

    @Column(name = "work_time_start")
    private Time workTimeStart;

    @Column(name = "work_time_end")
    private Time workTimeEnd;
}
