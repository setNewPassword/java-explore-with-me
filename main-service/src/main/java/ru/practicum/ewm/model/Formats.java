package ru.practicum.ewm.model;

import java.time.format.DateTimeFormatter;

public final class Formats {
    public static final String DATE = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE);
}