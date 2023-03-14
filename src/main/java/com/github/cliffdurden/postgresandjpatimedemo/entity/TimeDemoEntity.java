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
            name = "created_at_instant",
            columnDefinition = "timestamp default now()"
    )
    private Instant createdAtInstant;

    @Column(
            name = "created_at_instant_tz",
            columnDefinition = "timestamptz default now()"
    )
    private Instant createdAtInstantTz;

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
    @TimeZoneColumn(
            name = "created_at_odt_complex_offset",
            columnDefinition = "integer"
    )
    private OffsetDateTime createdAtOdtComplex;

    @Column(
            name = "created_at_odt_complex_tz",
            columnDefinition = "timestamptz default now()"
    )
    @TimeZoneStorage(TimeZoneStorageType.COLUMN)
    @TimeZoneColumn(
            name = "created_at_odt_complex_tz_offset",
            columnDefinition = "integer"
    )
    private OffsetDateTime createdAtOdtComplexTz;

    @Column(
            name = "created_at_zdt_complex",
            columnDefinition = "timestamp default now()"
    )
    @TimeZoneStorage(TimeZoneStorageType.COLUMN)
    @TimeZoneColumn(
            name = "created_at_zdt_complex_offset",
            columnDefinition = "integer"
    )
    private ZonedDateTime createdAtZdtComplex;

    @Column(
            name = "created_at_zdt_complex_tz",
            columnDefinition = "timestamptz default now()"
    )
    @TimeZoneStorage(TimeZoneStorageType.COLUMN)
    @TimeZoneColumn(
            name = "created_at_zdt_complex_tz_offset",
            columnDefinition = "integer"
    )
    private ZonedDateTime createdAtZdtComplexTz;

    @Column(
            name = "created_at_odt_native",
            columnDefinition = "timestamp default now()"
    )
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private OffsetDateTime createdAtOdtNative;

    @Column(
            name = "created_at_odt_native_tz",
            columnDefinition = "timestamptz default now()"
    )
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private OffsetDateTime createdAtOdtNativeTz;

    @Column(
            name = "created_at_odt_normalize",
            columnDefinition = "timestamp default now()"
    )
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private OffsetDateTime createdAtOdtNormalize;

    @Column(
            name = "created_at_odt_normalize_tz",
            columnDefinition = "timestamptz default now()"
    )
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private OffsetDateTime createdAtOdtNormalizeTz;

    @Column(
            name = "created_at_odt_normalize_utc",
            columnDefinition = "timestamp default now()"
    )
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE_UTC)
    private OffsetDateTime createdAtOdtNormalizeUtc;

    @Column(
            name = "created_at_odt_normalize_utc_tz",
            columnDefinition = "timestamptz default now()"
    )
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE_UTC)
    private OffsetDateTime createdAtOdtNormalizeUtcTz;
    @Column(
            name = "created_at_odt_auto",
            columnDefinition = "timestamp default now()"
    )
    @TimeZoneStorage(TimeZoneStorageType.AUTO)
    private OffsetDateTime createdAtOdtAuto;

    @Column(
            name = "created_at_odt_auto_tz",
            columnDefinition = "timestamptz default now()"
    )
    @TimeZoneStorage(TimeZoneStorageType.AUTO)
    private OffsetDateTime createdAtOdtAutoTz;

}
