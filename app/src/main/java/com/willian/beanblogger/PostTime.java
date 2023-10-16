package com.willian.beanblogger;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PostTime {

    private final Instant instant;
    private final String date;
    private final String time;

    public PostTime() {
        this.instant = Instant.now();

        var dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            .withZone(ZoneId.systemDefault());
        var timeFormat = DateTimeFormatter.ofPattern("HH:mm")
            .withZone(ZoneId.systemDefault());

        this.date = dateFormat.format(this.instant);
        this.time = timeFormat.format(this.instant);
    }

    public String getDate() { return this.date; }
    public String getTime() { return this.time; }
}
