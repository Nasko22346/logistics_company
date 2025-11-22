package org.informatics.logistics_company.model.jpa;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;

@Entity
@Table(name = "open_time")
@Data
public class OpenTime {
    @Id
    @Column(name = "worktime_id")
    private Long worktimeId;

    @Column(name = "worktime_day_of_week")
    private Integer worktimeDayOfWeek;

    @Column(name = "worktime_start")
    private Time worktimeStart;

    @Column(name = "worktime_end")
    private Time worktimeEnd;
}
