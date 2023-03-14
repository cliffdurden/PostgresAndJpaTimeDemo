# This demo shows how different time zones affects on different temporal java types.

## Prerequisites
- Installed docker is required to run tests.

## Preconditions
- jvm timezone is set to Asia/Kolkata via gradle test task properties
- postgres is run in container with env TZ=America/Los_Angeles 
- spring.jpa.properties.hibernate.jdbc.time_zone=Australia/Sydney
- spring.datasource.hikari.connection-init-sql=SET TIME ZONE 'Australia/Sydney'

## Test executions

### 1. This example shows different outcome whe temporal fields set by jvm with constant time

- time is constant CURRENT_TIME
- insert entity with temporal fields filled with CURRENT_TIME
- select and print results
- change JVM TimeZone
- insert entity with temporal fields filled with CURRENT_TIME
- select and print the results

#### Conditions

CURRENT_TIME = 2023-01-01T00:00:00.000000+09:00[Asia/Tokyo]

- Time at Asia/Tokyo: 2023-01-01T**00:00+09:00**[Asia/Tokyo]
- Time at Australia/Sydney: 2023-01-01T**02:00+11:00**[Australia/Sydney]
- Time at Asia/Kolkata: 2022-12-31T**20:30+05:30**[Asia/Kolkata]
- Time at Europe/London: 2022-12-31T**15:00Z**[Europe/London]
- Time at America/New_York: 2022-12-31T**10:00-05:00**[America/New_York]
- Time at America/Los_Angeles: 2022-12-31T**07:00-08:00**[America/Los_Angeles]

#### The results:

##### The result when TimeZone was set by default [Asia/Kolkata]

- Asia/Tokyo - CURRENT_TIME variable tz
- Australia/Sydney - set in spring.* connection properties
- Asia/Kolkata - current jvm timezone set via systemProperty 'user.timezone', 'Asia/Kolkata'
- Europe/London - UTC
- America/Los_Angeles - PostgreSQL TZ set .withEnv on container

| Field                                | PostgreSql  | Java               | Value                                  | Comments           |
|:-------------------------------------|:------------|:-------------------|:---------------------------------------|:-------------------|
| id                                   | bigint      | Long               | 1                                      |                    |
| created\_at\_ldt                     | timestamp   | LocalDateTime      | 2023-01-01T00:00                       | [Asia/Tokyo]       |
| created\_at\_ldt\_tz                 | timestamptz | LocalDateTime      | 2023-01-01T00:00                       | [Asia/Tokyo]       |
| created\_at\_odt                     | timestamp   | OffsetDateTime     | 2022-12-31T20:30+05:30                 | _[Asia/Kolkata]_   |
| created\_at\_odt\_tz                 | timestamptz | OffsetDateTime     | 2022-12-31T20:30+05:30                 | _[Asia/Kolkata]_   |
| created\_at\_zdt                     | timestamp   | ZonedDateTime      | 2022-12-31T20:30+05:30\[Asia/Kolkata\] | [Asia/Kolkata]     |
| created\_at\_zdt\_tz                 | timestamptz | ZonedDateTime      | 2022-12-31T20:30+05:30\[Asia/Kolkata\] | [Asia/Kolkata]     |
| created\_at\_instant                 | timestamp   | Instant            | 2023-01-01T02:00:00Z                   | [Australia/Sydney] |
| created\_at\_instant\_tz             | timestamptz | Instant            | 2022-12-31T15:00:00Z                   | [UTC]              |
| created\_at\_tstmp                   | timestamp   | java.sql.Timestamp | 2022-12-31 20:30:00.0                  | [Asia/Kolkata]     |
| created\_at\_tstmp\_tz               | timestamptz | java.sql.Timestamp | 2022-12-31 20:30:00.0                  | [Asia/Kolkata]     |
| created\_at\_odt\_complex            | timestamp   | OffsetDateTime     | 2023-01-01T11:00+09:00                 | **[ERROR]**        |
| created\_at\_odt\_complex\_tz        | timestamptz | OffsetDateTime     | 2023-01-01T00:00+09:00                 | [Asia/Tokyo]       |
| created\_at\_zdt\_complex            | timestamp   | ZonedDateTime      | 2023-01-01T11:00+09:00                 | **[ERROR]**        |
| created\_at\_zdt\_complex\_tz        | timestamptz | ZonedDateTime      | 2023-01-01T00:00+09:00                 | [Asia/Tokyo]       |
| created\_at\_odt\_native             | timestamp   | OffsetDateTime     | 2023-01-01T02:00Z                      | [Australia/Sydney] |
| created\_at\_odt\_native\_tz         | timestamptz | OffsetDateTime     | 2022-12-31T15:00Z                      | [UTC]              |
| created\_at\_odt\_normalize          | timestamp   | OffsetDateTime     | 2022-12-31T20:30+05:30                 | [Asia/Kolkata]     |
| created\_at\_odt\_normalize\_tz      | timestamptz | OffsetDateTime     | 2022-12-31T20:30+05:30                 | [Asia/Kolkata]     |
| created\_at\_odt\_normalize\_utc     | timestamp   | OffsetDateTime     | 2023-01-01T02:00Z                      | [Australia/Sydney] |
| created\_at\_odt\_normalize\_utc\_tz | timestamptz | OffsetDateTime     | 2022-12-31T15:00Z                      | [UTC]              |
| created\_at\_odt\_auto               | timestamp   | OffsetDateTime     | 2022-12-31T20:30+05:30                 | [Asia/Kolkata]     |
| created\_at\_odt\_auto\_tz           | timestamptz | OffsetDateTime     | 2022-12-31T20:30+05:30                 | [Asia/Kolkata]     |


##### The results after TimeZone has been changed to [Asia/Tokyo]

- Asia/Tokyo - CURRENT_TIME variable tz / current jvm TimeZone set via TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));
- Australia/Sydney - set in spring.* connection properties
- Asia/Kolkata - previous current jvm timezone set via systemProperty 'user.timezone', 'Asia/Kolkata'
- America/Los_Angeles - PostgreSQL TZ set .withEnv on container

| Field                                | PostgreSql  | Java               | Value when<br/>JVM tz = [Asia/Tokyo] | Comments                                 | Value when<br/>JVM tz = [Asia/Kolkata]<br/>(From previous table)<br/> |
|:-------------------------------------|:------------|:-------------------|:-------------------------------------|:-----------------------------------------|:----------------------------------------------------------------------|
| id                                   | bigint      | Long               | 1                                    |                                          | 1                                                                     |
| created\_at\_ldt                     | timestamp   | LocalDateTime      | 2023-01-01T03:30                     | [Asia/Tokyo]       -> Maybe Kolkata?     | 2023-01-01T00:00                                                      |
| created\_at\_ldt\_tz                 | timestamptz | LocalDateTime      | 2023-01-01T03:30                     | [Asia/Tokyo]       -> Maybe Kolkata?     | 2023-01-01T00:00                                                      |
| created\_at\_odt                     | timestamp   | OffsetDateTime     | 2023-01-01T00:00+09:00               | _[Asia/Kolkata]_   -> [Asia/Tokyo]       | 2022-12-31T20:30+05:30                                                |
| created\_at\_odt\_tz                 | timestamptz | OffsetDateTime     | 2023-01-01T00:00+09:00               | _[Asia/Kolkata]_   -> [Asia/Tokyo]       | 2022-12-31T20:30+05:30                                                |
| created\_at\_zdt                     | timestamp   | ZonedDateTime      | 2023-01-01T00:00+09:00\[Asia/Tokyo\] | [Asia/Kolkata]     -> _[Asia/Tokyo]_     | 2022-12-31T20:30+05:30\[Asia/Kolkata\]                                |
| created\_at\_zdt\_tz                 | timestamptz | ZonedDateTime      | 2023-01-01T00:00+09:00\[Asia/Tokyo\] | [Asia/Kolkata]     -> _[Asia/Tokyo]_     | 2022-12-31T20:30+05:30\[Asia/Kolkata\]                                |
| created\_at\_instant                 | timestamp   | Instant            | 2023-01-01T02:00:00Z                 | [Australia/Sydney] -> [Australia/Sydney] | 2023-01-01T02:00:00Z                                                  |
| created\_at\_instant\_tz             | timestamptz | Instant            | 2022-12-31T15:00:00Z                 | [UTC]              -> [UTC]              | 2022-12-31T15:00:00Z                                                  |
| created\_at\_tstmp                   | timestamp   | java.sql.Timestamp | 2023-01-01 00:00:00.0                | [Asia/Kolkata]     -> [Asia/Tokyo]       | 2022-12-31 20:30:00.0                                                 |
| created\_at\_tstmp\_tz               | timestamptz | java.sql.Timestamp | 2023-01-01 00:00:00.0                | [Asia/Kolkata]     -> [Asia/Tokyo]       | 2022-12-31 20:30:00.0                                                 |
| created\_at\_odt\_complex            | timestamp   | OffsetDateTime     | 2023-01-01T11:00+09:00               | **[ERROR]**        -> **[ERROR]**        | 2023-01-01T11:00+09:00                                                |
| created\_at\_odt\_complex\_tz        | timestamptz | OffsetDateTime     | 2023-01-01T00:00+09:00               | [Asia/Tokyo]       -> [Asia/Tokyo]       | 2023-01-01T00:00+09:00                                                |
| created\_at\_zdt\_complex            | timestamp   | ZonedDateTime      | 2023-01-01T11:00+09:00               | **[ERROR]**        -> **[ERROR]**        | 2023-01-01T11:00+09:00                                                |
| created\_at\_zdt\_complex\_tz        | timestamptz | ZonedDateTime      | 2023-01-01T00:00+09:00               | [Asia/Tokyo]       -> [Asia/Tokyo]       | 2023-01-01T00:00+09:00                                                |
| created\_at\_odt\_native             | timestamp   | OffsetDateTime     | 2023-01-01T02:00Z                    | [Australia/Sydney] -> [Australia/Sydney] | 2023-01-01T02:00Z                                                     |
| created\_at\_odt\_native\_tz         | timestamptz | OffsetDateTime     | 2022-12-31T15:00Z                    | [UTC]              -> [UTC]              | 2022-12-31T15:00Z                                                     |
| created\_at\_odt\_normalize          | timestamp   | OffsetDateTime     | 2023-01-01T00:00+09:00               | [Asia/Kolkata]     -> [Asia/Tokyo]       | 2022-12-31T20:30+05:30                                                |
| created\_at\_odt\_normalize\_tz      | timestamptz | OffsetDateTime     | 2023-01-01T00:00+09:00               | [Asia/Kolkata]     -> [Asia/Tokyo]       | 2022-12-31T20:30+05:30                                                |
| created\_at\_odt\_normalize\_utc     | timestamp   | OffsetDateTime     | 2023-01-01T02:00Z                    | [Australia/Sydney] -> [Australia/Sydney] | 2023-01-01T02:00Z                                                     |
| created\_at\_odt\_normalize\_utc\_tz | timestamptz | OffsetDateTime     | 2022-12-31T15:00Z                    | [UTC]              -> [UTC]              | 2022-12-31T15:00Z                                                     |
| created\_at\_odt\_auto               | timestamp   | OffsetDateTime     | 2023-01-01T00:00+09:00               | [Asia/Kolkata]     -> [Asia/Tokyo]       | 2022-12-31T20:30+05:30                                                |
| created\_at\_odt\_auto\_tz           | timestamptz | OffsetDateTime     | 2023-01-01T00:00+09:00               | [Asia/Kolkata]     -> [Asia/Tokyo]       | 2022-12-31T20:30+05:30                                                |
| id                                   | bigint      | Long               | 2                                    |                                          |
| created\_at\_ldt                     | timestamp   | LocalDateTime      | 2023-01-01T00:00                     | [Asia/Tokyo]                             |
| created\_at\_ldt\_tz                 | timestamptz | LocalDateTime      | 2023-01-01T00:00                     | [Asia/Tokyo]                             |
| created\_at\_odt                     | timestamp   | OffsetDateTime     | 2023-01-01T00:00+09:00               | [Asia/Tokyo]                             |
| created\_at\_odt\_tz                 | timestamptz | OffsetDateTime     | 2023-01-01T00:00+09:00               | [Asia/Tokyo]                             |
| created\_at\_zdt                     | timestamp   | ZonedDateTime      | 2023-01-01T00:00+09:00\[Asia/Tokyo\] | [Asia/Tokyo]                             |
| created\_at\_zdt\_tz                 | timestamptz | ZonedDateTime      | 2023-01-01T00:00+09:00\[Asia/Tokyo\] | [Asia/Tokyo]                             |
| created\_at\_instant                 | timestamp   | Instant            | 2023-01-01T02:00:00Z                 | [Australia/Sydney]                       |
| created\_at\_instant\_tz             | timestamptz | Instant            | 2022-12-31T15:00:00Z                 | [UTC]                                    |
| created\_at\_tstmp                   | timestamp   | java.sql.Timestamp | 2023-01-01 00:00:00.0                | [Asia/Tokyo]                             |
| created\_at\_tstmp\_tz               | timestamptz | java.sql.Timestamp | 2023-01-01 00:00:00.0                | [Asia/Tokyo]                             |
| created\_at\_odt\_complex            | timestamp   | OffsetDateTime     | 2023-01-01T11:00+09:00               | **[ERROR]**                              |
| created\_at\_odt\_complex\_tz        | timestamptz | OffsetDateTime     | 2023-01-01T00:00+09:00               | [Asia/Tokyo]                             |
| created\_at\_zdt\_complex            | timestamp   | ZonedDateTime      | 2023-01-01T11:00+09:00               | **[ERROR]**                              |
| created\_at\_zdt\_complex\_tz        | timestamptz | ZonedDateTime      | 2023-01-01T00:00+09:00               | [Asia/Tokyo]                             |
| created\_at\_odt\_native             | timestamp   | OffsetDateTime     | 2023-01-01T02:00Z                    | [Australia/Sydney]                       |
| created\_at\_odt\_native\_tz         | timestamptz | OffsetDateTime     | 2022-12-31T15:00Z                    | [UTC]                                    |
| created\_at\_odt\_normalize          | timestamp   | OffsetDateTime     | 2023-01-01T00:00+09:00               | [Asia/Tokyo]                             |
| created\_at\_odt\_normalize\_tz      | timestamptz | OffsetDateTime     | 2023-01-01T00:00+09:00               | [Asia/Tokyo]                             |
| created\_at\_odt\_normalize\_utc     | timestamp   | OffsetDateTime     | 2023-01-01T02:00Z                    | [Australia/Sydney]                       |
| created\_at\_odt\_normalize\_utc\_tz | timestamptz | OffsetDateTime     | 2022-12-31T15:00Z                    | [UTC]                                    |
| created\_at\_odt\_auto               | timestamp   | OffsetDateTime     | 2023-01-01T00:00+09:00               | [Asia/Tokyo]                             |
| created\_at\_odt\_auto\_tz           | timestamptz | OffsetDateTime     | 2023-01-01T00:00+09:00               | [Asia/Tokyo]                             |


------------------------------------------------------------------------------------------------------

### 2. This example shows different outcome when temporal fields are set by jvm and postgres with now()

- insert entity with temporal fields filled with null (all values are set by defaults at DB side)
- insert entity with temporal fields filled with now()
- select and print results
- change JVM TimeZone
- insert entity with temporal fields filled with null (all values are set by defaults at DB side)
- insert entity with temporal fields filled with now()
- select and print the results

#### Conditions

**World time in different time zones at the moment of the test execution**

- Asia/Tokyo: 2023-03-14T05:53:27.611200+09:00[Asia/Tokyo]
- Australia/Sydney: 2023-03-14T07:53:27.611343+11:00[Australia/Sydney]
- Asia/Kolkata: 2023-03-14T02:23:27.612068+05:30[Asia/Kolkata]
- UTC: 2023-03-13T20:53:27.612457Z[Europe/London]
- America/New_York: 2023-03-13T16:53:27.612909-04:00[America/New_York]
- America/Los_Angeles: 2023-03-13T13:53:27.613324-07:00[America/Los_Angeles]

#### The results:

##### The result when TimeZone was set by default [Asia/Kolkata]

JVM.now() at zone Asia/Kolkata: 2023-03-14T02:23:27.766348+05:30

| Field                         | PostgreSql  | Java               | Value                                                                                                      |
|:------------------------------|:------------|:-------------------|:-----------------------------------------------------------------------------------------------------------|
| id                            | bigint      | Long               | 1                                                                                                          |
| sample\_name                  | varchar2    | String             | JVM time: Asia/Kolkata\[2023-03-14T02:23:27.709533+05:30\]<br/>all values are set by postgres \(defaults\) |
| created\_at\_ldt              | timestamp   | LocalDateTime      | 2023-03-14T02:23:27.                                                                                       |
| created\_at\_ldt\_tz          | timestamptz | LocalDateTime      | 2023-03-14T02:23:27.                                                                                       |
| created\_at\_odt              | timestamp   | OffsetDateTime     | 2023-03-14T02:23:27.725945+05:                                                                             |
| created\_at\_odt\_tz          | timestamptz | OffsetDateTime     | 2023-03-14T02:23:27.725945+05:                                                                             |
| created\_at\_zdt              | timestamp   | ZonedDateTime      | 2023-03-14T02:23:27.725945+05:30\[Asia/Kolkata                                                             |
| created\_at\_zdt\_tz          | timestamptz | ZonedDateTime      | 2023-03-14T02:23:27.725945+05:30\[Asia/Kolkata                                                             |
| created\_at\_tstmp            | timestamp   | java.sql.Timestamp | 2023-03-14 02:23:27.                                                                                       |
| created\_at\_tstmp\_tz        | timestamptz | java.sql.Timestamp | 2023-03-14 02:23:27.                                                                                       |
| created\_at\_odt\_complex     | timestamp   | OffsetDateTime     | 2023-03-14T06:53:27.725945-01:                                                                             |
| created\_at\_odt\_complex\_tz | timestamptz | OffsetDateTime     | 2023-03-13T19:53:27.725945-01:                                                                             |
| created\_at\_zdt\_complex     | timestamptz | ZonedDateTime      | 2023-03-14T06:53:27.725945-01:                                                                             |
| created\_at\_zdt\_complex\_tz | timestamptz | ZonedDateTime      | 2023-03-13T19:53:27.725945-01:                                                                             |
|                               |             |                    |                                                                                                            |
| id                            | bigint      | Long               | 2                                                                                                          |
| sample\_name                  | varchar2    | String             | JVM time: Asia/Kolkata\[2023-03-14T02:23:27.738146+05:30\]<br/>all values are set by jvm now\(\)           |
| created\_at\_ldt              | timestamp   | LocalDateTime      | 2023-03-14T02:23:27.                                                                                       |
| created\_at\_ldt\_tz          | timestamptz | LocalDateTime      | 2023-03-14T02:23:27.                                                                                       |
| created\_at\_odt              | timestamp   | OffsetDateTime     | 2023-03-14T02:23:27.738293+05:                                                                             |
| created\_at\_odt\_tz          | timestamptz | OffsetDateTime     | 2023-03-14T02:23:27.738296+05:                                                                             |
| created\_at\_zdt              | timestamp   | ZonedDateTime      | 2023-03-14T02:23:27.738303+05:30\[Asia/Kolkata                                                             |
| created\_at\_zdt\_tz          | timestamptz | ZonedDateTime      | 2023-03-14T02:23:27.738306+05:30\[Asia/Kolkata                                                             |
| created\_at\_tstmp            | timestamp   | java.sql.Timestamp | 2023-03-14 02:23:27.                                                                                       |
| created\_at\_tstmp\_tz        | timestamptz | java.sql.Timestamp | 2023-03-14 02:23:27.                                                                                       |
| created\_at\_odt\_complex     | timestamp   | OffsetDateTime     | 2023-03-14T13:23:27.738336+05:                                                                             |
| created\_at\_odt\_complex\_tz | timestamptz | OffsetDateTime     | 2023-03-14T02:23:27.738339+05:                                                                             |
| created\_at\_zdt\_complex     | timestamptz | ZonedDateTime      | 2023-03-14T13:23:27.738341+05:                                                                             |
| created\_at\_zdt\_complex\_tz | timestamptz | ZonedDateTime      | 2023-03-14T02:23:27.738343+05:                                                                             |


##### The results after TimeZone has been changed to [Asia/Tokyo]

JVM.now() at zone Asia/Tokyo: 2023-03-14T05:53:27.794200+09:00

| Field                         | PostgreSql  | Java               | Value                                                                                                      |
|:------------------------------|:------------|:-------------------|:-----------------------------------------------------------------------------------------------------------|
| id                            | bigint      | Long               | 1                                                                                                          |
| sample\_name                  | varchar2    | String             | JVM time: Asia/Kolkata\[2023-03-14T02:23:27.709533+05:30\]<br/>all values are set by postgres \(defaults\) |
| created\_at\_ldt              | timestamp   | LocalDateTime      | 2023-03-14T05:53:27.725945                                                                                 |
| created\_at\_ldt\_tz          | timestamptz | LocalDateTime      | 2023-03-14T05:53:27.725945                                                                                 |
| created\_at\_odt              | timestamp   | OffsetDateTime     | 2023-03-14T05:53:27.725945+09:00                                                                           |
| created\_at\_odt\_tz          | timestamptz | OffsetDateTime     | 2023-03-14T05:53:27.725945+09:00                                                                           |
| created\_at\_zdt              | timestamp   | ZonedDateTime      | 2023-03-14T05:53:27.725945+09:00\[Asia/Tokyo\]                                                             |
| created\_at\_zdt\_tz          | timestamptz | ZonedDateTime      | 2023-03-14T05:53:27.725945+09:00\[Asia/Tokyo\]                                                             |
| created\_at\_tstmp            | timestamp   | java.sql.Timestamp | 2023-03-14 05:53:27.                                                                                       |
| created\_at\_tstmp\_tz        | timestamptz | java.sql.Timestamp | 2023-03-14 05:53:27.                                                                                       |
| created\_at\_odt\_complex     | timestamp   | OffsetDateTime     | 2023-03-14T06:53:27.725945-01:                                                                             |
| created\_at\_odt\_complex\_tz | timestamptz | OffsetDateTime     | 2023-03-13T19:53:27.725945-01:                                                                             |
| created\_at\_zdt\_complex     | timestamptz | ZonedDateTime      | 2023-03-14T06:53:27.725945-01:                                                                             |
| created\_at\_zdt\_complex\_tz | timestamptz | ZonedDateTime      | 2023-03-13T19:53:27.725945-01:                                                                             |
|                               |             |                    |                                                                                                            |
| id                            | bigint      | Long               | 2                                                                                                          |
| sample\_name                  | varchar2    | String             | JVM time: Asia/Kolkata\[2023-03-14T02:23:27.738146+05:30\]<br/>all values are set by jvm now\(\)           |
| created\_at\_ldt              | timestamp   | LocalDateTime      | 2023-03-14T05:53:27.                                                                                       |
| created\_at\_ldt\_tz          | timestamptz | LocalDateTime      | 2023-03-14T05:53:27.                                                                                       |
| created\_at\_odt              | timestamp   | OffsetDateTime     | 2023-03-14T05:53:27.738293+09:                                                                             |
| created\_at\_odt\_tz          | timestamptz | OffsetDateTime     | 2023-03-14T05:53:27.738296+09:                                                                             |
| created\_at\_zdt              | timestamp   | ZonedDateTime      | 2023-03-14T05:53:27.738303+09:00\[Asia/Tokyo                                                               |
| created\_at\_zdt\_tz          | timestamptz | ZonedDateTime      | 2023-03-14T05:53:27.738306+09:00\[Asia/Tokyo                                                               |
| created\_at\_tstmp            | timestamp   | java.sql.Timestamp | 2023-03-14 05:53:27.                                                                                       |
| created\_at\_tstmp\_tz        | timestamptz | java.sql.Timestamp | 2023-03-14 05:53:27.                                                                                       |
| created\_at\_odt\_complex     | timestamp   | OffsetDateTime     | 2023-03-14T13:23:27.738336+05:                                                                             |
| created\_at\_odt\_complex\_tz | timestamptz | OffsetDateTime     | 2023-03-14T02:23:27.738339+05:                                                                             |
| created\_at\_zdt\_complex     | timestamptz | ZonedDateTime      | 2023-03-14T13:23:27.738341+05:                                                                             |
| created\_at\_zdt\_complex\_tz | timestamptz | ZonedDateTime      | 2023-03-14T02:23:27.738343+05:                                                                             |
|                               |             |                    |                                                                                                            |
| id                            | bigint      | Long               | 3                                                                                                          |
| sample\_name                  | varchar2    | String             | JVM time: Asia/Tokyo\[2023-03-14T05:53:27.775029+09:00\]<br/>all values are set by postgres \(defaults\)   |
| created\_at\_ldt              | timestamp   | LocalDateTime      | 2023-03-14T05:53:27.                                                                                       |
| created\_at\_ldt\_tz          | timestamptz | LocalDateTime      | 2023-03-14T05:53:27.                                                                                       |
| created\_at\_odt              | timestamp   | OffsetDateTime     | 2023-03-14T05:53:27.776744+09:                                                                             |
| created\_at\_odt\_tz          | timestamptz | OffsetDateTime     | 2023-03-14T05:53:27.776744+09:                                                                             |
| created\_at\_zdt              | timestamp   | ZonedDateTime      | 2023-03-14T05:53:27.776744+09:00\[Asia/Tokyo                                                               |
| created\_at\_zdt\_tz          | timestamptz | ZonedDateTime      | 2023-03-14T05:53:27.776744+09:00\[Asia/Tokyo                                                               |
| created\_at\_tstmp            | timestamp   | java.sql.Timestamp | 2023-03-14 05:53:27.                                                                                       |
| created\_at\_tstmp\_tz        | timestamptz | java.sql.Timestamp | 2023-03-14 05:53:27.                                                                                       |
| created\_at\_odt\_complex     | timestamp   | OffsetDateTime     | 2023-03-14T06:53:27.776744-01:                                                                             |
| created\_at\_odt\_complex\_tz | timestamptz | OffsetDateTime     | 2023-03-13T19:53:27.776744-01:                                                                             |
| created\_at\_zdt\_complex     | timestamptz | ZonedDateTime      | 2023-03-14T06:53:27.776744-01:                                                                             |
| created\_at\_zdt\_complex\_tz | timestamptz | ZonedDateTime      | 2023-03-13T19:53:27.776744-01:                                                                             |
|                               |             |                    |                                                                                                            |
| id                            | bigint      | Long               | 4                                                                                                          |
| sample\_name                  | varchar2    | String             | JVM time: Asia/Tokyo\[2023-03-14T05:53:27.780056+09:00\]<br/>all values are set by jvm now\(\)             |
| created\_at\_ldt              | timestamp   | LocalDateTime      | 2023-03-14T05:53:27.                                                                                       |
| created\_at\_ldt\_tz          | timestamptz | LocalDateTime      | 2023-03-14T05:53:27.                                                                                       |
| created\_at\_odt              | timestamp   | OffsetDateTime     | 2023-03-14T05:53:27.780098+09:                                                                             |
| created\_at\_odt\_tz          | timestamptz | OffsetDateTime     | 2023-03-14T05:53:27.780100+09:                                                                             |
| created\_at\_zdt              | timestamp   | ZonedDateTime      | 2023-03-14T05:53:27.780125+09:00\[Asia/Tokyo                                                               |
| created\_at\_zdt\_tz          | timestamptz | ZonedDateTime      | 2023-03-14T05:53:27.780131+09:00\[Asia/Tokyo                                                               |
| created\_at\_tstmp            | timestamp   | java.sql.Timestamp | 2023-03-14 05:53:27.                                                                                       |
| created\_at\_tstmp\_tz        | timestamptz | java.sql.Timestamp | 2023-03-14 05:53:27.                                                                                       |
| created\_at\_odt\_complex     | timestamp   | OffsetDateTime     | 2023-03-14T16:53:27.780138+09:                                                                             |
| created\_at\_odt\_complex\_tz | timestamptz | OffsetDateTime     | 2023-03-14T05:53:27.780141+09:                                                                             |
| created\_at\_zdt\_complex     | timestamptz | ZonedDateTime      | 2023-03-14T16:53:27.780143+09:                                                                             |
| created\_at\_zdt\_complex\_tz | timestamptz | ZonedDateTime      | 2023-03-14T05:53:27.780150+09:                                                                             |



