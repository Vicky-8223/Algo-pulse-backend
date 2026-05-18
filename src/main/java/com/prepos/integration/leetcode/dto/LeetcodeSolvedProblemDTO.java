package com.prepos.integration.leetcode.dto;

import lombok.Data;

import java.util.List;

@Data
public class LeetcodeSolvedProblemDTO {
    private String title;
    private String titleSlug;
    private String difficulty;
    private List<String> topicTags;
}


