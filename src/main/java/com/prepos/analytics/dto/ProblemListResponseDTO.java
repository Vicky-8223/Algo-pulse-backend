package com.prepos.analytics.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProblemListResponseDTO {
    private List<ProblemSummaryDTO> problemsetQuestionList;
}
