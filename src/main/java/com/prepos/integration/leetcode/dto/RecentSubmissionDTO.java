package com.prepos.integration.leetcode.dto;

import lombok.Data;

@Data
public class RecentSubmissionDTO {

    private String title;

    private String titleSlug;

    private String timestamp;

    private String statusDisplay;

    private String lang;
}