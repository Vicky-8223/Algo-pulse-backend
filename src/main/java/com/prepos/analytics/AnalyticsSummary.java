package com.prepos.analytics;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AnalyticsSummary {
      private Integer totalSolved;
      private Integer easySolved;
      private Integer mediumSolved;
      private Integer hardSolved;
      private Integer acceptanceRate;
      private Integer strongTopics;
      private Integer weakTopics;
      private List<String>strongTopicsList;
      private List<String>weakTopicsList;
}
