package com.prepos.analytics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DifficultyAnalyticsDTO {

    private int easySolved;

    private int mediumSolved;

    private int hardSolved;

    private int totalSolved;

    private double easyPercentage;

    private double mediumPercentage;

    private double hardPercentage;
}