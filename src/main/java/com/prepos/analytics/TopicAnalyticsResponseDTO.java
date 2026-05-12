package com.prepos.analytics;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TopicAnalyticsResponseDTO {
    private String topic;
    private Integer solvedCount;
    private String strengthLevel;
    private Integer revisionPriority;

}
