package com.prepos.integration.leetcode.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prepos.integration.leetcode.client.LeetcodeClient;
import com.prepos.integration.leetcode.dto.LeetcodeProfileDTO;
import com.prepos.integration.leetcode.dto.SkillResponseDTO;
import com.prepos.integration.leetcode.dto.SolvedStatsDTO;
import lombok.RequiredArgsConstructor;
//import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

@Service
@RequiredArgsConstructor
public class LeetcodeService {
    private final LeetcodeClient leetcodeClient;
    private final ObjectMapper objectMapper;
    @Cacheable(
            value="leetcode_profile",
            key="#username"
    )
    public LeetcodeProfileDTO getUserProfile(String username){
        try{
            String response = leetcodeClient.getUserProfile(username);
            return objectMapper.readValue(response,
                    LeetcodeProfileDTO.class);
        }
        catch(Exception e){
            throw new RuntimeException("Failed to fetch Leetcode Profile");
        }
    }
    public SkillResponseDTO fetchSkill(String username){
        try{
            String response=leetcodeClient.getSkillStat(username);
            //System.out.println(response);
            return objectMapper.readValue(response,SkillResponseDTO.class);
        }
        catch(Exception e){
            throw new RuntimeException("Failed to fetch Skill stat");
        }
    }
}
