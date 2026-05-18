package com.prepos.analytics;

import com.prepos.analytics.dto.*;
import com.prepos.auth.entity.User;
import com.prepos.auth.repository.UserRepository;
import com.prepos.integration.leetcode.client.LeetcodeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    private final UserRepository userRepository;
    private final LeetcodeClient leetcodeClient;

    @PostMapping("/sync/{username}")
    public String syncAnalytics(@PathVariable String username, Authentication authentication){
        String email=authentication.getName();
        User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        analyticsService.syncUserAnalytics(user,username);
        return "Analytics synced successfully";
    }
    @GetMapping("/me")
     public List<TopicAnalyticsResponseDTO> getMyAnalytics(Authentication authentication){
           String email=authentication.getName();
           User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
           return analyticsService.getUserAnalytics(user);
     }
//     @GetMapping("/recommendations")
//    public List<RecommendationDTO> getRecommendations(Authentication authentication){
//        String email=authentication.getName();
//        User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
//        return analyticsService.getRecommendations(user);
//     }
    @GetMapping("/summary")
    public AnalyticsSummary getSummary(
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        return analyticsService.getSummary(
                user
        );
    }
    @GetMapping("/calendar")
    public CalendarDTO test(Authentication authentication)throws Exception{
        String email=authentication.getName();
        User user=userRepository.findByEmail(email).orElseThrow();
        return analyticsService.getCalendar(user);
    }
    @GetMapping("/difficulty")
    public DifficultyAnalyticsDTO getDifficulty(Authentication authentication){
        String email=authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        return analyticsService.getDifficultyAnalytics(user);
    }
    @GetMapping("/recommendations")
    public List<RecommendedProblemDTO>getRecommendations(Authentication authentication){
        String email=authentication.getName();
        User user=userRepository.findByEmail(email).orElseThrow();
        return analyticsService.getRecommendations(user);
    }
    @GetMapping("/weakness")
    public List<WeaknessDTO> getWeaknessAnalytics(Authentication authentication){
        String email=authentication.getName();
        User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        return analyticsService.getWeaknessAnalytics(user);
    }
    @GetMapping("/solved-problems")
    public List<SolvedProblemDTO>getSolvedProblems(Authentication authentication){
        String email=authentication.getName();
        User user=userRepository.findByEmail(email).orElseThrow();
        return analyticsService.getSolvedProblems(user);
    }

}


