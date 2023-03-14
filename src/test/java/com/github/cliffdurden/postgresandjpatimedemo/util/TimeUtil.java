package com.github.cliffdurden.postgresandjpatimedemo.util;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.util.TimeZone;

import static com.github.cliffdurden.postgresandjpatimedemo.util.TerminalColorUtil.ANSI_YELLOW;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtil {

    public static void printWorldCurrentTime() {
        var consoleColor = ANSI_YELLOW;
        log.info("{}===============================================================================", consoleColor);
        log.info("{}Time at Asia/Tokyo: {}", consoleColor, ZonedDateTime.now(ZoneId.of("Asia/Tokyo")));
        log.info("{}Time at Australia/Sydney: {}", consoleColor, ZonedDateTime.now(ZoneId.of("Australia/Sydney")));
        log.info("{}Time at Asia/Kolkata: {}", consoleColor, ZonedDateTime.now(ZoneId.of("Asia/Kolkata")));
        log.info("{}Time at Europe/London: {}", consoleColor, ZonedDateTime.now(ZoneId.of("Europe/London")));
        log.info("{}Time at America/New_York: {}", consoleColor, ZonedDateTime.now(ZoneId.of("America/New_York")));
        log.info("{}Time at America/Los_Angeles: {}", consoleColor, ZonedDateTime.now(ZoneId.of("America/Los_Angeles")));
        log.info("{}===============================================================================", consoleColor);
    }

    public static void printWorldTime(String zonedDateTime) {
        var consoleColor = ANSI_YELLOW;
        log.info("{}===============================================================================", consoleColor);
        log.info("{}Time at Asia/Tokyo: {}", consoleColor, ZonedDateTime.parse(zonedDateTime).withZoneSameInstant(ZoneId.of("Asia/Tokyo")));
        log.info("{}Time at Australia/Sydney: {}", consoleColor, ZonedDateTime.parse(zonedDateTime).withZoneSameInstant(ZoneId.of("Australia/Sydney")));
        log.info("{}Time at Asia/Kolkata: {}", consoleColor, ZonedDateTime.parse(zonedDateTime).withZoneSameInstant(ZoneId.of("Asia/Kolkata")));
        log.info("{}Time at Europe/London: {}", consoleColor, ZonedDateTime.parse(zonedDateTime).withZoneSameInstant(ZoneId.of("Europe/London")));
        log.info("{}Time at America/New_York: {}", consoleColor, ZonedDateTime.parse(zonedDateTime).withZoneSameInstant(ZoneId.of("America/New_York")));
        log.info("{}Time at America/Los_Angeles: {}", consoleColor, ZonedDateTime.parse(zonedDateTime).withZoneSameInstant(ZoneId.of("America/Los_Angeles")));
        log.info("{}===============================================================================", consoleColor);
    }

    public static String defaultTimeZoneId() {
        return TimeZone.getDefault().getID();
    }

}
