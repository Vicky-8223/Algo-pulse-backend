package com.prepos.integration.leetcode.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SkillResponseDTO {
    private List<SkillTagDTO> fundamental;
    private List<SkillTagDTO> intermediate;
    private List<SkillTagDTO> advanced;
}
