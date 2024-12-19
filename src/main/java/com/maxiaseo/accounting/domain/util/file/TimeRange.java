package com.maxiaseo.accounting.domain.util.file;

import java.time.LocalDateTime;

public class TimeRange {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public TimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}