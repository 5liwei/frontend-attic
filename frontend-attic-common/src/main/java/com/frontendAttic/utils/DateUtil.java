package com.frontendAttic.utils;

import com.frontendAttic.entity.enums.DateTimePatternEnum;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class DateUtil {

    private static final ConcurrentHashMap<String, ThreadLocal<SimpleDateFormat>> sdfMap = new ConcurrentHashMap<>();

    private static SimpleDateFormat getSdf(String pattern) {
        return sdfMap.computeIfAbsent(pattern, k -> ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern))).get();
    }

    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    public static Date parse(String dateString, String pattern) {
        try {
            return getSdf(pattern).parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Failed to parse date: " + dateString, e);
        }
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date minusDays(int days) {
        return Date.from(LocalDateTime.now().minusDays(days).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static List<String> getDatesInRange(Date startDate, Date endDate) {
        LocalDate start = toLocalDate(startDate);
        LocalDate end = toLocalDate(endDate);
        long daysBetween = ChronoUnit.DAYS.between(start, end);

        return Stream.iterate(start, date -> date.plusDays(1))
                     .limit(daysBetween + 1)
                     .map(date -> date.format(DateTimeFormatter.ofPattern(DateTimePatternEnum.YYYY_MM_DD.getPattern())))
                     .collect(Collectors.toList());
    }
}
