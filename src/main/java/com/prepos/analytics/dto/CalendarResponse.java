package com.prepos.analytics.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalendarResponse {
    private List<Integer> activeYears;
    private int streak;
    private int totalActiveDays;
    private String submissionCalendar;
}
