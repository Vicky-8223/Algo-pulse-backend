package com.prepos.integration.leetcode.dto;

import lombok.Data;

import java.util.List;

@Data
public class AcSubmissionResponseDTO {

    private int count;

    private List<RecentSubmissionDTO>
            submission;
}