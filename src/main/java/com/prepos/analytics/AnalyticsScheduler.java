package com.prepos.analytics;

import com.prepos.auth.entity.User;
import com.prepos.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnalyticsScheduler {
    private final UserRepository userRepository;
    private final AnalyticsService analyticsService;

    @Scheduled(fixedRate=86400)
    public void runAnalyticsScheduler(){
        log.info("Starting analytics scheduler");
        List<User> users=userRepository.findAll();
        for(User user:users){
            try{
                String leetcodeUsername=user.getLeetcodeUserName();
                if(leetcodeUsername==null||leetcodeUsername.isBlank())continue;
                analyticsService.syncUserAnalytics(user,leetcodeUsername);
                log.info("Synced analytics for {}",user.getEmail());
            }
            catch(Exception e){
                log.error("Failed syncing user {}",user.getEmail());
            }
        }
    }
}
