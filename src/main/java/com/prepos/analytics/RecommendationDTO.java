package com.prepos.analytics;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RecommendationDTO {
 private String topic;
 private String message;
 private Integer priority;
}
