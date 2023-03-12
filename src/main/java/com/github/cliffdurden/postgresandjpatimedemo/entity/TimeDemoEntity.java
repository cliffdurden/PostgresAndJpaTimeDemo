package com.github.cliffdurden.postgresandjpatimedemo.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.*;

import static org.hibernate.annotations.TimeZoneStorageType.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "time_demo")
@DynamicInsert
public class TimeDemoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sample_name")
    private String sampleName;

    @Column(
            name = "created_at_without_tz",
            columnDefinition = "timestamp default now()"
    )
    private LocalDateTime createdAtWithoutTz;

    @Column(
            name = "created_at_with_tz",
            columnDefinition = "timestamptz default now()"
    )
    private LocalDateTime createdAtWithTz;

    @Column(
            name = "created_at_2_without_tz",
            columnDefinition = "timestamp default now()"
    )
    private OffsetDateTime createdAt2WithoutTz;

    @Column(
            name = "created_at_2_with_tz",
            columnDefinition = "timestamptz default now()"
    )
    private OffsetDateTime createdAt2WithTz;

    @Column(
            name = "created_at_3_without_tz",
            columnDefinition = "timestamp default now()"
    )
    private java.sql.Timestamp createdAt3WithoutTz;

    @Column(
            name = "created_at_3_with_tz",
            columnDefinition = "timestamptz default now()"
    )
    private java.sql.Timestamp createdAt3WithTz;

    @Column(
            name = "created_at_4_without_tz",
            columnDefinition = "timestamp default now()"
    )
    @TimeZoneStorage(TimeZoneStorageType.COLUMN)
    @TimeZoneColumn(name = "created_at_4_without_tz_offset")
    private OffsetDateTime createdAt4WithoutTz;

    @Column(
            name = "created_at_4_with_tz",
            columnDefinition = "timestamptz default now()"
    )
    @TimeZoneStorage(TimeZoneStorageType.COLUMN)
    @TimeZoneColumn(name = "created_at_4_with_tz_offset")
    private OffsetDateTime createdAt4WithTz;

}
