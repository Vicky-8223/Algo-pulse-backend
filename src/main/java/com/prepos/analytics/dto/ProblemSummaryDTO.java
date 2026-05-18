package com.prepos.analytics.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ProblemSummaryDTO {
    private String title;
    private String titleSlug;
    private String difficulty;
}
