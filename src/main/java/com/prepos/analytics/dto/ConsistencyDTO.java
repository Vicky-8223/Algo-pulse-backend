package com.prepos.analytics.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsistencyDTO {
    private int streak;
    private int totalActiveDays;
    private int totalSubmissions;
    private int maxDailySubmissions;
    private double consistencyScore;

}
