package com.github.cliffdurden.postgresandjpatimedemo.entity;

import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.*;


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
            name = "created_at_ldt",
            columnDefinition = "timestamp default now()"
    )
    private LocalDateTime createdAtLdt;

    @Column(
            name = "created_at_ldt_tz",
            columnDefinition = "timestamptz default now()"
    )
    private LocalDateTime createdAtLdtTz;

    @Column(
            name = "created_at_odt",
            columnDefinition = "timestamp default now()"
    )
    private OffsetDateTime createdAtOdt;

    @Column(
            name = "created_at_odt_tz",
            columnDefinition = "timestamptz default now()"
    )
    private OffsetDateTime createdAtOdtTz;

    @Column(
            name = "created_at_zdt",
            columnDefinition = "timestamp default now()"
    )
    private ZonedDateTime createdAtZdt;

    @Column(
            name = "created_at_zdt_tz",
            columnDefinition = "timestamptz default now()"
    )
    private ZonedDateTime createdAtZdtTz;

    @Column(
            name = "created_at_tstmp",
            columnDefinition = "timestamp default now()"
    )
    private java.sql.Timestamp createdAtTstmp;

    @Column(
            name = "created_at_tstmp_tz",
            columnDefinition = "timestamptz default now()"
    )
    private java.sql.Timestamp createdAtTstmpTz;

    @Column(
            name = "created_at_odt_complex",
            columnDefinition = "timestamp default now()"
    )
    @TimeZoneStorage(TimeZoneStorageType.COLUMN)
    @TimeZoneColumn(name = "created_at_odt_complex_offset")
    private OffsetDateTime createdAtOdtComplex;

    @Column(
            name = "created_at_odt_complex_tz",
            columnDefinition = "timestamptz default now()"
    )
    @TimeZoneStorage(TimeZoneStorageType.COLUMN)
    @TimeZoneColumn(name = "created_at_odt_complex_tz_offset")
    private OffsetDateTime createdAtOdtComplexTz;

    @Column(
            name = "created_at_zdt_complex",
            columnDefinition = "timestamp default now()"
    )
    @TimeZoneStorage(TimeZoneStorageType.COLUMN)
    @TimeZoneColumn(name = "created_at_zdt_complex_offset")
    private ZonedDateTime createdAtZdtComplex;

    @Column(
            name = "created_at_zdt_complex_tz",
            columnDefinition = "timestamptz default now()"
    )
    @TimeZoneStorage(TimeZoneStorageType.COLUMN)
    @TimeZoneColumn(name = "created_at_zdt_complex_tz_offset")
    private ZonedDateTime createdAtZdtComplexTz;

}
