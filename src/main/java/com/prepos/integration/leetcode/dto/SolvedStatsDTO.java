package com.prepos.integration.leetcode.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolvedStatsDTO {

    private Integer solvedProblem;

    private Integer easySolved;

    private Integer mediumSolved;

    private Integer hardSolved;

    private SubmissionStatsDTO[] totalSubmissionNum;

    private SubmissionStatsDTO[] acSubmissionNum;
}