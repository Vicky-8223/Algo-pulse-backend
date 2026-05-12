package com.prepos.analytics;

import com.prepos.auth.entity.User;
import com.prepos.auth.repository.UserRepository;
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
     @GetMapping("/recommendations")
    public List<RecommendationDTO> getRecommendations(Authentication authentication){
        String email=authentication.getName();
        User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        return analyticsService.getRecommendations(user);
     }
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
}


