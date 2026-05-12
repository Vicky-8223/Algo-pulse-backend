package com.prepos.integration.leetcode.client;

import com.prepos.integration.leetcode.dto.SolvedStatsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class LeetcodeClient {
    private final RestTemplate restTemplate;
    private  static final String BASE_URL=
            "https://alfa-leetcode-api.onrender.com";
    public String getUserProfile(String username){
        String url=BASE_URL+"/"+username+"/profile";
        return restTemplate.getForObject(url,String.class);
    }
    public String getSkillStat(String username){
        String url=BASE_URL+"/"+username+"/skill";
        return restTemplate.getForObject(url,String.class);
    }
    public SolvedStatsDTO fetchSolvedStats(
            String username
    ) {

        String url =
                BASE_URL + "/" + username + "/solved";

        return restTemplate.getForObject(
                url,
                SolvedStatsDTO.class
        );
    }
}
