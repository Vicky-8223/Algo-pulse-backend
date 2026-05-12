package com.prepos.integration.leetcode.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmissionStatsDTO {

    private String difficulty;

    private Integer count;

    private Integer submissions;
}