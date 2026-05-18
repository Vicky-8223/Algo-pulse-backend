package com.prepos.analytics.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MomentumDTO {
    private String topic;
    private double momentumScore;
    private String trend;
    private String insight;
}


