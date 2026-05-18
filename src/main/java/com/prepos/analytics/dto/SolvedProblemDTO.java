package com.prepos.analytics.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolvedProblemDTO {

    private String problemTitle;

    private String titleSlug;

    private String difficulty;

    private Integer likes;

    private LocalDateTime solvedAt;

    private List<String> topicTags;
}