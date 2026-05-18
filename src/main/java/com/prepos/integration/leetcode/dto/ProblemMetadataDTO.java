package com.prepos.integration.leetcode.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class ProblemMetadataDTO {
    private String difficulty;
    private boolean isPaidOnly;
    private int likes;
    private int dislikes;
    private List<TopicTagDTO> topicTags;

}
