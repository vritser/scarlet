package io.codewar.scarlet.infra.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record DateRange(LocalDateTime start, LocalDateTime end) implements Range<LocalDateTime> {
    public static DateRange today() {
        var today = LocalDate.now();
        var endOfDay = LocalTime.of(23, 59, 59);
        return new DateRange(today.atStartOfDay(), endOfDay.atDate(today));
    }
}
