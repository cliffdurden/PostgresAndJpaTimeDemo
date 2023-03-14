package com.github.cliffdurden.postgresandjpatimedemo;

import com.github.cliffdurden.postgresandjpatimedemo.entity.TimeDemoEntity;
import com.github.cliffdurden.postgresandjpatimedemo.repository.TimeDemoRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.cliffdurden.postgresandjpatimedemo.util.TerminalColorUtil.*;
import static com.github.cliffdurden.postgresandjpatimedemo.util.TimeUtil.*;


@Slf4j
@Testcontainers
@SpringBootTest(
        properties = {
                "spring.jpa.properties.hibernate.jdbc.time_zone=Australia/Sydney",
                "spring.datasource.hikari.connection-init-sql=SET TIME ZONE 'Australia/Sydney'"
//                "spring.datasource.hikari.connection-init-sql=SET TIME ZONE 'America/New_York'" // in this case LocalDateTime works wrong
        }
)
class PostgresAndJpaTimeDemoApplicationTests {


    @Autowired
    private TimeDemoRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * .withEnv("TZ", "America/Los_Angeles");
     * <p/>
     * setting TZ leads to changes in postgresql.conf
     * <p/>
     * cat /var/lib/postgresql/data/postgresql.conf | grep timezone
     * log_timezone = 'America/Los_Angeles'
     * timezone = 'America/Los_Angeles'
     */
    @Container
    static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:15.2")
            .withEnv("TZ", "America/Los_Angeles")
            .withReuse(true);

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("DB_URL", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("DB_USERNAME", POSTGRESQL_CONTAINER::getUsername);
        registry.add("DB_PASSWORD", POSTGRESQL_CONTAINER::getPassword);
        log.info("Database: {}", POSTGRESQL_CONTAINER.getJdbcUrl());
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void testDateTimeBehaviorWhenDateTimesArePredifined() {
        String localDateTime = "2023-01-01T00:00:00.000000";
        String zonedDateTime = "2023-01-01T00:00:00.000000+09:00[Asia/Tokyo]";
        String offsetDateTime = "2023-01-01T00:00:00.000000+09:00";
        printWorldTime(zonedDateTime);
        log.info(ANSI_RED + "Predefined Time is: {}", zonedDateTime);
        log.info(ANSI_RED + "JVM TimeZone is: {}", defaultTimeZoneId());
        repository.save(timeDemoEntityDateTime(
                localDateTime,
                offsetDateTime,
                zonedDateTime));
        var timeDemoEntities = repository.findAll();
        printEntities(timeDemoEntities);

        ////////////////////////////////////////////////////////////
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));
        ////////////////////////////////////////////////////////////
        log.info(ANSI_RED + "JVM TimeZone has been changed to: {}", defaultTimeZoneId());
        repository.save(timeDemoEntityDateTime(
                localDateTime,
                offsetDateTime,
                zonedDateTime));
        var timeDemoEntitiesAfterTzChanged = repository.findAll();
        printEntities(timeDemoEntitiesAfterTzChanged);
    }

    @Test
    void testDateTimeBehavior() {
        printWorldCurrentTime();
        log.info(ANSI_RED + "JVM TimeZone is: {}", defaultTimeZoneId());
        repository.save(timeDemoEntityDefaults());
        repository.save(timeDemoEntityJvmNow());
        var timeDemoEntities = repository.findAll();
        var resultFormattedOnDBSide = jdbcTemplate.queryForList(sqlQueryResultFormattedOnDbSide(), String.class);

        printEntities(timeDemoEntities);
        printPreFormattedResult(resultFormattedOnDBSide);

        ////////////////////////////////////////////////////////////
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));
        ////////////////////////////////////////////////////////////
        log.info(ANSI_RED + "JVM TimeZone has been changed to: {}", defaultTimeZoneId());
        repository.save(timeDemoEntityDefaults());
        repository.save(timeDemoEntityJvmNow());
        timeDemoEntities = repository.findAll();
        resultFormattedOnDBSide = jdbcTemplate.queryForList(sqlQueryResultFormattedOnDbSide(), String.class);

        printEntities(timeDemoEntities);
        printPreFormattedResult(resultFormattedOnDBSide);
    }

    /**
     * @return entity with empty datetime fields
     */
    private TimeDemoEntity timeDemoEntityDefaults() {
        return TimeDemoEntity.builder()
                .sampleName("JVM time: " + defaultTimeZoneId() + "[" + OffsetDateTime.now() + "]" + " - all values are set by postgres (defaults)")
                .build();
    }

    /**
     * @return entity with datetime fields filled with now()
     */
    private TimeDemoEntity timeDemoEntityJvmNow() {
        return TimeDemoEntity.builder()
                .sampleName("JVM time: " + defaultTimeZoneId() + "[" + OffsetDateTime.now() + "]" + " - all values are set by jvm now()")
                .createdAtLdt(LocalDateTime.now())
                .createdAtLdtTz(LocalDateTime.now())
                .createdAtOdt(OffsetDateTime.now())
                .createdAtOdtTz(OffsetDateTime.now())
                .createdAtZdt(ZonedDateTime.now())
                .createdAtZdtTz(ZonedDateTime.now())
                .createdAtInstant(Instant.now())
                .createdAtInstantTz(Instant.now())
                .createdAtTstmp(java.sql.Timestamp.from(Instant.now()))
                .createdAtTstmpTz(java.sql.Timestamp.from(Instant.now()))
                .createdAtOdtComplex(OffsetDateTime.now())
                .createdAtOdtComplexTz(OffsetDateTime.now())
                .createdAtZdtComplex(ZonedDateTime.now())
                .createdAtZdtComplexTz(ZonedDateTime.now())
                .createdAtOdtNative(OffsetDateTime.now())
                .createdAtOdtNativeTz(OffsetDateTime.now())
                .createdAtOdtNormalize(OffsetDateTime.now())
                .createdAtOdtNormalizeTz(OffsetDateTime.now())
                .createdAtOdtNormalizeUtc(OffsetDateTime.now())
                .createdAtOdtNormalizeUtcTz(OffsetDateTime.now())
                .createdAtOdtAuto(OffsetDateTime.now())
                .createdAtOdtAutoTz(OffsetDateTime.now())
                .build();
    }

    private TimeDemoEntity timeDemoEntityDateTime(
            String dateTime,
            String dateTimeWithOffset,
            String dateTimeWIthOffsetAndZone
    ) {
        return TimeDemoEntity.builder()
                .sampleName("JVM time: " + defaultTimeZoneId() + "[" + OffsetDateTime.now() + "]" + " - all values are set with constant")
                .createdAtLdt(LocalDateTime.parse(dateTime))
                .createdAtLdtTz(LocalDateTime.parse(dateTime))
                .createdAtOdt(OffsetDateTime.parse(dateTimeWithOffset))
                .createdAtOdtTz(OffsetDateTime.parse(dateTimeWithOffset))
                .createdAtZdt(ZonedDateTime.parse(dateTimeWIthOffsetAndZone))
                .createdAtZdtTz(ZonedDateTime.parse(dateTimeWIthOffsetAndZone))
                .createdAtInstant(Instant.parse(dateTimeWithOffset))
                .createdAtInstantTz(Instant.parse(dateTimeWithOffset))
                .createdAtTstmp(java.sql.Timestamp.from(Instant.parse(dateTimeWithOffset)))
                .createdAtTstmpTz(java.sql.Timestamp.from(Instant.parse(dateTimeWithOffset)))
                .createdAtOdtComplex(OffsetDateTime.parse(dateTimeWithOffset))
                .createdAtOdtComplexTz(OffsetDateTime.parse(dateTimeWithOffset))
                .createdAtZdtComplex(ZonedDateTime.parse(dateTimeWIthOffsetAndZone))
                .createdAtZdtComplexTz(ZonedDateTime.parse(dateTimeWIthOffsetAndZone))
                .createdAtOdtNative(OffsetDateTime.parse(dateTimeWithOffset))
                .createdAtOdtNativeTz(OffsetDateTime.parse(dateTimeWithOffset))
                .createdAtOdtNormalize(OffsetDateTime.parse(dateTimeWithOffset))
                .createdAtOdtNormalizeTz(OffsetDateTime.parse(dateTimeWithOffset))
                .createdAtOdtNormalizeUtc(OffsetDateTime.parse(dateTimeWithOffset))
                .createdAtOdtNormalizeUtcTz(OffsetDateTime.parse(dateTimeWithOffset))
                .createdAtOdtAuto(OffsetDateTime.parse(dateTimeWithOffset))
                .createdAtOdtAutoTz(OffsetDateTime.parse(dateTimeWithOffset))
                .build();
    }

    private void printEntities(List<TimeDemoEntity> entities) {
        log.info("\n{}FOUND ENTITIES WITH JPA HAVE BEEN DESERIALIZED BY JPA: \n{}", ANSI_BLUE,
                entities
                        .stream()
                        .map(this::formatTimeDemoEntity)
                        .collect(Collectors.joining("\n")));
    }

    private void printPreFormattedResult(List<String> resultFormattedOnDBSide) {
        log.info("\n{}FOUND ROWS WITH JDBC HAVE BEEN DESERIALIZED BY POSTGRESQL: \n{}{}", ANSI_BLUE,
                ANSI_PURPLE,
                String.join("\n", resultFormattedOnDBSide));
    }

    private String formatTimeDemoEntity(TimeDemoEntity entity) {
        return "" +
                ANSI_GREEN + "\tJVM.now() at zone " + defaultTimeZoneId() + ": " + OffsetDateTime.now() + "\n" +
                ANSI_GREEN + "\t-----------------------------------------------------------------------------------------------------\n" +
                ANSI_GREEN + "\tField                           | PostgreSql  |               Java | Value\n" +
                ANSI_GREEN + "\t--------------------------------|-------------|--------------------|---------------------------------\n" +
                ANSI_GREEN + "\tid                              | bigint      |               Long | " + entity.getId() + "\n" +
                ANSI_GREEN + "\tsample_name                     | varchar2    |             String | " + entity.getSampleName() + "\n" +
                ANSI_GREEN + "\tcreated_at_ldt                  | timestamp   |      LocalDateTime | " + entity.getCreatedAtLdt() + "\n" +
                ANSI_GREEN + "\tcreated_at_ldt_tz               | timestamptz |      LocalDateTime | " + entity.getCreatedAtLdtTz() + "\n" +
                ANSI_GREEN + "\tcreated_at_odt                  | timestamp   |     OffsetDateTime | " + entity.getCreatedAtOdt() + "\n" +
                ANSI_GREEN + "\tcreated_at_odt_tz               | timestamptz |     OffsetDateTime | " + entity.getCreatedAtOdtTz() + "\n" +
                ANSI_GREEN + "\tcreated_at_zdt                  | timestamp   |      ZonedDateTime | " + entity.getCreatedAtZdt() + "\n" +
                ANSI_GREEN + "\tcreated_at_zdt_tz               | timestamptz |      ZonedDateTime | " + entity.getCreatedAtZdtTz() + "\n" +
                ANSI_GREEN + "\tcreated_at_instant              | timestamp   |            Instant | " + entity.getCreatedAtInstant() + "\n" +
                ANSI_GREEN + "\tcreated_at_instant_tz           | timestamptz |            Instant | " + entity.getCreatedAtInstantTz() + "\n" +
                ANSI_GREEN + "\tcreated_at_tstmp                | timestamp   | java.sql.Timestamp | " + entity.getCreatedAtTstmp() + "\n" +
                ANSI_GREEN + "\tcreated_at_tstmp_tz             | timestamptz | java.sql.Timestamp | " + entity.getCreatedAtTstmpTz() + "\n" +
                ANSI_GREEN + "\tcreated_at_odt_complex          | timestamp   |     OffsetDateTime | " + entity.getCreatedAtOdtComplex() + "\n" +
                ANSI_GREEN + "\tcreated_at_odt_complex_tz       | timestamptz |     OffsetDateTime | " + entity.getCreatedAtOdtComplexTz() + "\n" +
                ANSI_GREEN + "\tcreated_at_zdt_complex          | timestamp   |      ZonedDateTime | " + entity.getCreatedAtZdtComplex() + "\n" +
                ANSI_GREEN + "\tcreated_at_zdt_complex_tz       | timestamptz |      ZonedDateTime | " + entity.getCreatedAtZdtComplexTz() + "\n" +
                ANSI_GREEN + "\tcreated_at_odt_native           | timestamp   |     OffsetDateTime | " + entity.getCreatedAtOdtNative() + "\n" +
                ANSI_GREEN + "\tcreated_at_odt_native_tz        | timestamptz |     OffsetDateTime | " + entity.getCreatedAtOdtNativeTz() + "\n" +
                ANSI_GREEN + "\tcreated_at_odt_normalize        | timestamp   |     OffsetDateTime | " + entity.getCreatedAtOdtNormalize() + "\n" +
                ANSI_GREEN + "\tcreated_at_odt_normalize_tz     | timestamptz |     OffsetDateTime | " + entity.getCreatedAtOdtNormalizeTz() + "\n" +
                ANSI_GREEN + "\tcreated_at_odt_normalize_utc    | timestamp   |     OffsetDateTime | " + entity.getCreatedAtOdtNormalizeUtc() + "\n" +
                ANSI_GREEN + "\tcreated_at_odt_normalize_utc_tz | timestamptz |     OffsetDateTime | " + entity.getCreatedAtOdtNormalizeUtcTz() + "\n" +
                ANSI_GREEN + "\tcreated_at_odt_auto             | timestamp   |     OffsetDateTime | " + entity.getCreatedAtOdtAuto() + "\n" +
                ANSI_GREEN + "\tcreated_at_odt_auto_tz          | timestamptz |     OffsetDateTime | " + entity.getCreatedAtOdtAutoTz() + "\n" +
                "";
    }

    @NotNull
    private static String sqlQueryResultFormattedOnDbSide() {
        return "" +
                "select " +
                "'" + ANSI_PURPLE + "\tJVM.now() at zone " + defaultTimeZoneId() + ": " + OffsetDateTime.now() + "\n' ||" +
                "'" + ANSI_PURPLE + "\t---------------------------------------------------------------------------------\n' ||" +
                "'" + ANSI_PURPLE + "\tField                            | PostgreSql  | Value \n' ||" +
                "'" + ANSI_PURPLE + "\t---------------------------------|-------------|---------------------------------\n' ||" +
                "'" + ANSI_PURPLE + "\tid                               | bigint      | ' || id || '\n' ||" +
                "'" + ANSI_PURPLE + "\tsample_name                      | varchar2    | ' || sample_name || '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_ldt                   | timestamp   | ' || created_at_ldt ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_ldt_tz                | timestamptz | ' || created_at_ldt_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_odt                   | timestamp   | ' || created_at_odt ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_odt_tz                | timestamptz | ' || created_at_odt_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_zdt                   | timestamp   | ' || created_at_zdt ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_zdt_tz                | timestamptz | ' || created_at_zdt_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_instant               | timestamp   | ' || created_at_zdt_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_instant_tz            | timestamptz | ' || created_at_zdt_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_tstmp                 | timestamp   | ' || created_at_tstmp ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_tstmp_tz              | timestamptz | ' || created_at_tstmp_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_odt_complex           | timestamp   | ' || created_at_odt_complex ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_odt_complex_offset    | integer     | ' || created_at_odt_complex_offset ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_odt_complex_tz        | timestamptz | ' || created_at_odt_complex_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_odt_complex_tz_offset | integer     | ' || created_at_odt_complex_tz_offset ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_zdt_complex           | timestamp   | ' || created_at_zdt_complex ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_zdt_complex_offset    | integer     | ' || created_at_zdt_complex_offset ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_zdt_complex_tz        | timestamptz | ' || created_at_zdt_complex_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_zdt_complex_tz_offset | integer     | ' || created_at_zdt_complex_tz_offset ||  '\n' " +
                "'" + ANSI_PURPLE + "\tcreated_at_odt_native            | timestamp   | ' || created_at_odt_native ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_odt_native_tz         | timestamptz | ' || created_at_odt_native_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_odt_normalize         | timestamp   | ' || created_at_odt_normalize ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_odt_normalize_tz      | timestamptz | ' || created_at_odt_normalize_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_odt_normalize_utc     | timestamp   | ' || created_at_odt_normalize_utc ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_odt_normalize_utc_tz  | timestamptz | ' || created_at_odt_normalize_utc_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_odt_auto              | timestamp   | ' || created_at_odt_auto ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_odt_auto_tz           | timestamptz | ' || created_at_odt_auto_tz  ||  '\n' ||" +
                "from time_demo";
    }

}
