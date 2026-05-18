package com.prepos.analytics.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder

public class DashboardSummaryDTO {
    private Integer totalSolved;
    private Integer easySolved;
    private Integer mediumSolved;
    private Integer hardSolved;
    private Double acceptanceRate;
    private Integer strongTopicCount;
    private Integer weakTopicCount;
    private List<String> strongTopics;
    private List<String> weakTopics;
}
