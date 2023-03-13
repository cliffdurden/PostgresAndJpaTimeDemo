# This demo shows how different time zones affects on different temporal java types.

## Prerequisites
- Installed docker is required to run tests.

## Preconditions
- jvm timezone is set to Asia/Kolkata via gradle test task properties
- postgres is run in container with env TZ=America/Los_Angeles 
- spring.jpa.properties.hibernate.jdbc.time_zone=Australia/Sydney
- spring.datasource.hikari.connection-init-sql=SET TIME ZONE 'Australia/Sydney'

## Test execution
- insert entity with temporal fields filled with null (all values are set by defaults at DB side)
- insert entity with temporal fields filled with now()
- select and print results
- change JVM TimeZone
- insert entity with temporal fields filled with null (all values are set by defaults at DB side)
- insert entity with temporal fields filled with now()
- select and print results

# The results
## Real time on different timezones
Asia/Tokyo: 2023-03-14T05:53:27.611200+09:00[Asia/Tokyo]

Australia/Sydney: 2023-03-14T07:53:27.611343+11:00[Australia/Sydney]

Asia/Kolkata: 2023-03-14T02:23:27.612068+05:30[Asia/Kolkata]

Europe/London: 2023-03-13T20:53:27.612457Z[Europe/London]

America/New_York: 2023-03-13T16:53:27.612909-04:00[America/New_York]

America/Los_Angeles: 2023-03-13T13:53:27.613324-07:00[America/Los_Angeles]


## The result when TimeZone was set by default [Asia/Kolkata]

`JVM.now() at zone Asia/Kolkata: 2023-03-14T02:23:27.766348+05:30`

| Field                         | PostgreSql  | Java               | Value                                                                                                    |
|:------------------------------|:------------|:-------------------|:---------------------------------------------------------------------------------------------------------|
| id                            | bigint      | Long               | 1                                                                                                        |
| sample\_name                  | varchar2    | String             | JVM time: Asia/Kolkata\[2023-03-14T02:23:27.709533+05:30\] - all values are set by postgres \(defaults\) |
| created\_at\_ldt              | timestamp   | LocalDateTime      | `2023-03-14T02:23:27.725945                      `                                                       |
| created\_at\_ldt\_tz          | timestamptz | LocalDateTime      | `2023-03-14T02:23:27.725945                      `                                                       |
| created\_at\_odt              | timestamp   | OffsetDateTime     | `2023-03-14T02:23:27.725945+05:30                `                                                       |
| created\_at\_odt\_tz          | timestamptz | OffsetDateTime     | `2023-03-14T02:23:27.725945+05:30                `                                                       |
| created\_at\_zdt              | timestamp   | ZonedDateTime      | `2023-03-14T02:23:27.725945+05:30\[Asia/Kolkata\]`                                                       |
| created\_at\_zdt\_tz          | timestamptz | ZonedDateTime      | `2023-03-14T02:23:27.725945+05:30\[Asia/Kolkata\]`                                                       |
| created\_at\_tstmp            | timestamp   | java.sql.Timestamp | `2023-03-14 02:23:27.725945                      `                                                       |
| created\_at\_tstmp\_tz        | timestamptz | java.sql.Timestamp | `2023-03-14 02:23:27.725945                      `                                                       |
| created\_at\_odt\_complex     | timestamp   | OffsetDateTime     | `2023-03-14T06:53:27.725945-01:00                `                                                       |
| created\_at\_odt\_complex\_tz | timestamptz | OffsetDateTime     | `2023-03-13T19:53:27.725945-01:00                `                                                       |
| created\_at\_zdt\_complex     | timestamptz | ZonedDateTime      | `2023-03-14T06:53:27.725945-01:00                `                                                       |
| created\_at\_zdt\_complex\_tz | timestamptz | ZonedDateTime      | `2023-03-13T19:53:27.725945-01:00                `                                                       |
|                               |             |                    |                                                                                                          |
| id                            | bigint      | Long               | 2                                                                                                        |
| sample\_name                  | varchar2    | String             | JVM time: Asia/Kolkata\[2023-03-14T02:23:27.738146+05:30\] - all values are set by jvm now\(\)           |
| created\_at\_ldt              | timestamp   | LocalDateTime      | `2023-03-14T02:23:27.`                                                                                   |
| created\_at\_ldt\_tz          | timestamptz | LocalDateTime      | `2023-03-14T02:23:27.`                                                                                   |
| created\_at\_odt              | timestamp   | OffsetDateTime     | `2023-03-14T02:23:27.738293+05:`                                                                         |
| created\_at\_odt\_tz          | timestamptz | OffsetDateTime     | `2023-03-14T02:23:27.738296+05:`                                                                         |
| created\_at\_zdt              | timestamp   | ZonedDateTime      | `2023-03-14T02:23:27.738303+05:30\[Asia/Kolkata`                                                         |
| created\_at\_zdt\_tz          | timestamptz | ZonedDateTime      | `2023-03-14T02:23:27.738306+05:30\[Asia/Kolkata`                                                         |
| created\_at\_tstmp            | timestamp   | java.sql.Timestamp | `2023-03-14 02:23:27.`                                                                                   |
| created\_at\_tstmp\_tz        | timestamptz | java.sql.Timestamp | `2023-03-14 02:23:27.`                                                                                   |
| created\_at\_odt\_complex     | timestamp   | OffsetDateTime     | `2023-03-14T13:23:27.738336+05:`                                                                         |
| created\_at\_odt\_complex\_tz | timestamptz | OffsetDateTime     | `2023-03-14T02:23:27.738339+05:`                                                                         |
| created\_at\_zdt\_complex     | timestamptz | ZonedDateTime      | `2023-03-14T13:23:27.738341+05:`                                                                         |
| created\_at\_zdt\_complex\_tz | timestamptz | ZonedDateTime      | `2023-03-14T02:23:27.738343+05:`                                                                         |


## The results after TimeZone has been changed to [Asia/Tokyo]

`JVM.now() at zone Asia/Tokyo: 2023-03-14T05:53:27.794200+09:00`

| Field                         | PostgreSql  | Java               | Value                                                                                                    |
|:------------------------------|:------------|:-------------------|:---------------------------------------------------------------------------------------------------------|
| id                            | bigint      | Long               | 1                                                                                                        |
| sample\_name                  | varchar2    | String             | JVM time: Asia/Kolkata\[2023-03-14T02:23:27.709533+05:30\] - all values are set by postgres \(defaults\) |
| created\_at\_ldt              | timestamp   | LocalDateTime      | `2023-03-14T05:53:27.725945`                                                                             |
| created\_at\_ldt\_tz          | timestamptz | LocalDateTime      | `2023-03-14T05:53:27.725945`                                                                             |
| created\_at\_odt              | timestamp   | OffsetDateTime     | `2023-03-14T05:53:27.725945+09:00`                                                                       |
| created\_at\_odt\_tz          | timestamptz | OffsetDateTime     | `2023-03-14T05:53:27.725945+09:00`                                                                       |
| created\_at\_zdt              | timestamp   | ZonedDateTime      | `2023-03-14T05:53:27.725945+09:00\[Asia/Tokyo\]`                                                         |
| created\_at\_zdt\_tz          | timestamptz | ZonedDateTime      | `2023-03-14T05:53:27.725945+09:00\[Asia/Tokyo\]`                                                         |
| created\_at\_tstmp            | timestamp   | java.sql.Timestamp | `2023-03-14 05:53:27.`                                                                                   |
| created\_at\_tstmp\_tz        | timestamptz | java.sql.Timestamp | `2023-03-14 05:53:27.`                                                                                   |
| created\_at\_odt\_complex     | timestamp   | OffsetDateTime     | `2023-03-14T06:53:27.725945-01:`                                                                         |
| created\_at\_odt\_complex\_tz | timestamptz | OffsetDateTime     | `2023-03-13T19:53:27.725945-01:`                                                                         |
| created\_at\_zdt\_complex     | timestamptz | ZonedDateTime      | `2023-03-14T06:53:27.725945-01:`                                                                         |
| created\_at\_zdt\_complex\_tz | timestamptz | ZonedDateTime      | `2023-03-13T19:53:27.725945-01:`                                                                         |
|                               |             |                    |                                                                                                          |
| id                            | bigint      | Long               | 2                                                                                                        |
| sample\_name                  | varchar2    | String             | JVM time: Asia/Kolkata\[2023-03-14T02:23:27.738146+05:30\] - all values are set by jvm now\(\)           |
| created\_at\_ldt              | timestamp   | LocalDateTime      | `2023-03-14T05:53:27.`                                                                                   |
| created\_at\_ldt\_tz          | timestamptz | LocalDateTime      | `2023-03-14T05:53:27.`                                                                                   |
| created\_at\_odt              | timestamp   | OffsetDateTime     | `2023-03-14T05:53:27.738293+09:`                                                                         |
| created\_at\_odt\_tz          | timestamptz | OffsetDateTime     | `2023-03-14T05:53:27.738296+09:`                                                                         |
| created\_at\_zdt              | timestamp   | ZonedDateTime      | `2023-03-14T05:53:27.738303+09:00\[Asia/Tokyo`                                                           |
| created\_at\_zdt\_tz          | timestamptz | ZonedDateTime      | `2023-03-14T05:53:27.738306+09:00\[Asia/Tokyo`                                                           |
| created\_at\_tstmp            | timestamp   | java.sql.Timestamp | `2023-03-14 05:53:27.`                                                                                   |
| created\_at\_tstmp\_tz        | timestamptz | java.sql.Timestamp | `2023-03-14 05:53:27.`                                                                                   |
| created\_at\_odt\_complex     | timestamp   | OffsetDateTime     | `2023-03-14T13:23:27.738336+05:`                                                                         |
| created\_at\_odt\_complex\_tz | timestamptz | OffsetDateTime     | `2023-03-14T02:23:27.738339+05:`                                                                         |
| created\_at\_zdt\_complex     | timestamptz | ZonedDateTime      | `2023-03-14T13:23:27.738341+05:`                                                                         |
| created\_at\_zdt\_complex\_tz | timestamptz | ZonedDateTime      | `2023-03-14T02:23:27.738343+05:`                                                                         |
|                               |             |                    |                                                                                                          |
| id                            | bigint      | Long               | 3                                                                                                        |
| sample\_name                  | varchar2    | String             | JVM time: Asia/Tokyo\[2023-03-14T05:53:27.775029+09:00\] - all values are set by postgres \(defaults\)   |
| created\_at\_ldt              | timestamp   | LocalDateTime      | `2023-03-14T05:53:27.`                                                                                   |
| created\_at\_ldt\_tz          | timestamptz | LocalDateTime      | `2023-03-14T05:53:27.`                                                                                   |
| created\_at\_odt              | timestamp   | OffsetDateTime     | `2023-03-14T05:53:27.776744+09:`                                                                         |
| created\_at\_odt\_tz          | timestamptz | OffsetDateTime     | `2023-03-14T05:53:27.776744+09:`                                                                         |
| created\_at\_zdt              | timestamp   | ZonedDateTime      | `2023-03-14T05:53:27.776744+09:00\[Asia/Tokyo`                                                           |
| created\_at\_zdt\_tz          | timestamptz | ZonedDateTime      | `2023-03-14T05:53:27.776744+09:00\[Asia/Tokyo`                                                           |
| created\_at\_tstmp            | timestamp   | java.sql.Timestamp | `2023-03-14 05:53:27.`                                                                                   |
| created\_at\_tstmp\_tz        | timestamptz | java.sql.Timestamp | `2023-03-14 05:53:27.`                                                                                   |
| created\_at\_odt\_complex     | timestamp   | OffsetDateTime     | `2023-03-14T06:53:27.776744-01:`                                                                         |
| created\_at\_odt\_complex\_tz | timestamptz | OffsetDateTime     | `2023-03-13T19:53:27.776744-01:`                                                                         |
| created\_at\_zdt\_complex     | timestamptz | ZonedDateTime      | `2023-03-14T06:53:27.776744-01:`                                                                         |
| created\_at\_zdt\_complex\_tz | timestamptz | ZonedDateTime      | `2023-03-13T19:53:27.776744-01:`                                                                         |
|                               |             |                    |                                                                                                          |
| id                            | bigint      | Long               | 4                                                                                                        |
| sample\_name                  | varchar2    | String             | JVM time: Asia/Tokyo\[2023-03-14T05:53:27.780056+09:00\] - all values are set by jvm now\(\)             |
| created\_at\_ldt              | timestamp   | LocalDateTime      | `2023-03-14T05:53:27.`                                                                                   |
| created\_at\_ldt\_tz          | timestamptz | LocalDateTime      | `2023-03-14T05:53:27.`                                                                                   |
| created\_at\_odt              | timestamp   | OffsetDateTime     | `2023-03-14T05:53:27.780098+09:`                                                                         |
| created\_at\_odt\_tz          | timestamptz | OffsetDateTime     | `2023-03-14T05:53:27.780100+09:`                                                                         |
| created\_at\_zdt              | timestamp   | ZonedDateTime      | `2023-03-14T05:53:27.780125+09:00\[Asia/Tokyo`                                                           |
| created\_at\_zdt\_tz          | timestamptz | ZonedDateTime      | `2023-03-14T05:53:27.780131+09:00\[Asia/Tokyo`                                                           |
| created\_at\_tstmp            | timestamp   | java.sql.Timestamp | `2023-03-14 05:53:27.`                                                                                   |
| created\_at\_tstmp\_tz        | timestamptz | java.sql.Timestamp | `2023-03-14 05:53:27.`                                                                                   |
| created\_at\_odt\_complex     | timestamp   | OffsetDateTime     | `2023-03-14T16:53:27.780138+09:`                                                                         |
| created\_at\_odt\_complex\_tz | timestamptz | OffsetDateTime     | `2023-03-14T05:53:27.780141+09:`                                                                         |
| created\_at\_zdt\_complex     | timestamptz | ZonedDateTime      | `2023-03-14T16:53:27.780143+09:`                                                                         |
| created\_at\_zdt\_complex\_tz | timestamptz | ZonedDateTime      | `2023-03-14T05:53:27.780150+09:`                                                                         |



