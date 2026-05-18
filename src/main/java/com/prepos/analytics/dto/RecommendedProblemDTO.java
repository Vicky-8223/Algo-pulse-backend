package com.prepos.analytics.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RecommendedProblemDTO {
    private String title;
    private String titleSlug;
    private String difficulty;
    private String topic;
    private String reason;
    private double priorityScore;


}
