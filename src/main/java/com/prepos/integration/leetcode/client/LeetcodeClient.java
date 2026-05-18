package com.prepos.integration.leetcode.client;

import com.prepos.analytics.dto.CalendarDTO;
import com.prepos.analytics.dto.CalendarResponse;
import com.prepos.analytics.dto.ProblemListResponseDTO;
import com.prepos.integration.leetcode.dto.*;
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
    public CalendarResponse getCalendar(String username){
        String url=BASE_URL+"/"+username+"/calendar";
        return restTemplate.getForObject(url,CalendarResponse.class);
    }
    public AcSubmissionResponseDTO getAcceptedSubmissions(String username){
        String url=BASE_URL+"/"+username+"/acSubmission?limit=50";
        return restTemplate.getForObject(url,AcSubmissionResponseDTO.class);
    }

    public ProblemMetadataDTO getProblemMetadata(String titleSlug){
        String url=BASE_URL+"/select?titleSlug="+titleSlug;
        return restTemplate.getForObject(url,ProblemMetadataDTO.class);

    }
    public ProblemListResponseDTO getProblemByTopic(String topic,String difficulty){
        String url=BASE_URL+"/problems?tags="+topic+"&difficulty="+difficulty+"&limit=20";
        return restTemplate.getForObject(url,ProblemListResponseDTO.class);
    }
}
