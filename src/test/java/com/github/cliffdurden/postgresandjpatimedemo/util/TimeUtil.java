package com.github.cliffdurden.postgresandjpatimedemo.util;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.util.TimeZone;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtil {

    public static void printCurrentTime() {
        log.info("===============================================================================");
        log.info("Time at Asia/Tokyo: {}", ZonedDateTime.now(ZoneId.of("Asia/Tokyo")));
        log.info("Time at Australia/Sydney: {}", ZonedDateTime.now(ZoneId.of("Australia/Sydney")));
        log.info("Time at Asia/Kolkata: {}", ZonedDateTime.now(ZoneId.of("Asia/Kolkata")));
        log.info("Time at Europe/London: {}", ZonedDateTime.now(ZoneId.of("Europe/London")));
        log.info("Time at America/New_York: {}", ZonedDateTime.now(ZoneId.of("America/New_York")));
        log.info("Time at America/Los_Angeles: {}", ZonedDateTime.now(ZoneId.of("America/Los_Angeles")));
        log.info("===============================================================================");
    }

    public static String defaultTimeZoneId() {
        return TimeZone.getDefault().getID();
    }

}
