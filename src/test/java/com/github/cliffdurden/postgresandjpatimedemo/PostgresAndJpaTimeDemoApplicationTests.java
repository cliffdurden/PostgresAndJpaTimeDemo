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
        printCurrentTime();
        repository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void testDateTimeBehavior() {
        log.info(ANSI_RED + "JVM TimeZone is: {}", defaultTimeZoneId());
        repository.save(timeDemoEntityDefaults());
        repository.save(timeDemoEntityJvmNow());
        jdbcTemplate.execute("insert into time_demo (sample_name) values ('TimeZone.getDefault().getID(): " + defaultTimeZoneId() + " - all values are set by defult. jdbcTemplate')");
        var timeDemoEntities = repository.findAll();
        var resultFormattedOnDBSide = jdbcTemplate.queryForList(sqlQueryResultFormattedOnDbSide(), String.class);

        printEntities(timeDemoEntities);
        printPreFormattedResult(resultFormattedOnDBSide);

        ////////////////////////////////////////////////////////////

        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));
        log.info(ANSI_RED + "JVM TimeZone has been changed to: {}", defaultTimeZoneId());
        repository.save(timeDemoEntityDefaults());
        repository.save(timeDemoEntityJvmNow());
        jdbcTemplate.execute("insert into time_demo (sample_name) values ('TimeZone.getDefault().getID(): " + defaultTimeZoneId() + " - all values are set by defult. jdbcTemplate')");
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
                .sampleName("TimeZone.getDefault().getID(): " + defaultTimeZoneId() + " - all values are set by postgres (defaults)")
                .build();
    }

    /**
     * @return entity with datetime fields filled with now()
     */
    private TimeDemoEntity timeDemoEntityJvmNow() {
        return TimeDemoEntity.builder()
                .sampleName("TimeZone.getDefault().getID(): " + defaultTimeZoneId() + " - all values are set by jvm now()")
                .createdAtWithoutTz(LocalDateTime.now())
                .createdAtWithTz(LocalDateTime.now())
                .createdAt2WithoutTz(OffsetDateTime.now())
                .createdAt2WithTz(OffsetDateTime.now())
                .createdAt3WithoutTz(java.sql.Timestamp.from(Instant.now()))
                .createdAt3WithTz(java.sql.Timestamp.from(Instant.now()))
                .createdAt4WithoutTz(OffsetDateTime.now())
                .createdAt4WithTz(OffsetDateTime.now())
                .build();
    }

    private void printEntities(List<TimeDemoEntity> entities) {
        log.info("\nFOUND ENTITIES WITH JPA: \n{}",
                entities
                        .stream()
                        .map(this::formatTimeDemoEntity)
                        .collect(Collectors.joining("\n")));
    }

    private void printPreFormattedResult(List<String> resultFormattedOnDBSide) {
        log.info("\nFOUND ROWS WITH JDBC [FORMATTED ON DB SIDE]: \n{}{}",
                ANSI_PURPLE,
                String.join("\n", resultFormattedOnDBSide));
    }

    private String formatTimeDemoEntity(TimeDemoEntity entity) {
        return "" +
                ANSI_GREEN + "sample_name: " + entity.getSampleName() + "\n" +
                ANSI_GREEN + "\tnow() at zone " + defaultTimeZoneId() + ": " + OffsetDateTime.now() + "\n" +
                ANSI_GREEN + "\tField                    | PostgreSql  |      Java          | Value\n" +
                ANSI_GREEN + "\t-------------------------|-------------|--------------------|---------------------------------\n" +
                ANSI_GREEN + "\tcreated_at_without_tz    | timestamp   |      LocalDateTime | " + entity.getCreatedAtWithoutTz() + "\n" +
                ANSI_GREEN + "\tcreated_at_with_tz       | timestamptz |      LocalDateTime | " + entity.getCreatedAtWithTz() + "\n" +
                ANSI_GREEN + "\tcreated_at_2_without_tz  | timestamp   |     OffsetDateTime | " + entity.getCreatedAt2WithoutTz() + "\n" +
                ANSI_GREEN + "\tcreated_at_2_with_tz     | timestamptz |     OffsetDateTime | " + entity.getCreatedAt2WithoutTz() + "\n" +
                ANSI_GREEN + "\tcreated_at_3_without_tz  | timestamp   | java.sql.Timestamp | " + entity.getCreatedAt3WithoutTz() + "\n" +
                ANSI_GREEN + "\tcreated_at_3_with_tz     | timestamptz | java.sql.Timestamp | " + entity.getCreatedAt3WithTz() + "\n" +
                ANSI_GREEN + "\tcreated_at_4_with_tz     | timestamp   |     OffsetDateTime | " + entity.getCreatedAt4WithTz() + "\n" +
                ANSI_GREEN + "\tcreated_at_4_with_tz     | timestamptz |     OffsetDateTime | " + entity.getCreatedAt4WithTz() + "\n" +
                "";
    }

    @NotNull
    private static String sqlQueryResultFormattedOnDbSide() {
        return "" +
                "select '" + ANSI_PURPLE + "sample_name: ' || sample_name || '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_without_tz          | timestamp   | ' || created_at_without_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_with_tz             | timestamptz | ' || created_at_with_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_2_without_tz        | timestamp   | ' || created_at_2_without_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_2_with_tz           | timestamptz | ' || created_at_2_with_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_3_without_tz        | timestamp   | ' || created_at_3_without_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_3_with_tz           | timestamptz | ' || created_at_3_with_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_4_without_tz        | timestamp   | ' || created_at_4_without_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_4_without_tz_offset | integer     | ' || created_at_4_without_tz_offset ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_4_with_tz           | timestamptz | ' || created_at_4_with_tz ||  '\n' ||" +
                "'" + ANSI_PURPLE + "\tcreated_at_4_with_tz_offset    | integer     | ' || created_at_4_with_tz_offset ||  '\n' " +
                "from time_demo";
    }

}
