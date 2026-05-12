package com.prepos.analytics;

import com.prepos.auth.entity.User;
import com.prepos.common.constants.AnalyticsConstants;
import com.prepos.integration.leetcode.client.LeetcodeClient;
import com.prepos.integration.leetcode.dto.SkillResponseDTO;
import com.prepos.integration.leetcode.dto.SkillTagDTO;
import com.prepos.integration.leetcode.dto.SolvedStatsDTO;
import com.prepos.integration.leetcode.service.LeetcodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AnalyticsService {
    private final UserTopicProgressRepository repo;
    private final LeetcodeService leetcodeService;
    private final LeetcodeClient leetcodeClient;

    private StrengthLevel calculateStrength(Integer solvedCount){
        if(solvedCount>=50)return StrengthLevel.STRONG;
        else if(solvedCount>=25)return StrengthLevel.MEDIUM;
        else return StrengthLevel.WEAK;
    }
    private Integer calculateRevisionPriority(Integer solvedCount) {
        if (solvedCount < 10) {
            return 10;
        }
        if (solvedCount < 30) {
            return 5;
        }
        return 1;
    }
    public void processSkillStats(User user, SkillResponseDTO response){
        Map<CoreTopic,Integer>topicCounts=new HashMap<>();
        processTags(response.getFundamental(),topicCounts);
        processTags(response.getIntermediate(),topicCounts);
        processTags(response.getAdvanced(),topicCounts);
        for(Map.Entry<CoreTopic,Integer>entry:topicCounts.entrySet()){
            CoreTopic topic=entry.getKey();
            Integer solved=entry.getValue();
            UserTopicProgress progress=repo.findByUserAndTopic(user,topic)
                    .orElse(UserTopicProgress.builder()
                            .user(user)
                            .topic(topic)
                            .build());
            Integer previousSolved=progress.getPreviousSolvedCount();
            if(previousSolved==null){
                progress.setDecayCounter(AnalyticsConstants.MAX_DECAY_COUNTER);
            }
            else if(solved>previousSolved){
               progress.setDecayCounter(AnalyticsConstants.MAX_DECAY_COUNTER);
               progress.setLastPracticeDate(LocalDate.now());
            }
            else{
                if(progress.getLastDecayUpdateDate()==null||!progress.getLastDecayUpdateDate().equals(LocalDate.now())){
                    Integer currentDecay=progress.getDecayCounter();
                    if(currentDecay==null){
                        currentDecay=AnalyticsConstants.MAX_DECAY_COUNTER;
                    }
                    progress.setDecayCounter(Math.max(currentDecay-1,0));
                    progress.setLastDecayUpdateDate(LocalDate.now());
                }
            }
            progress.setPreviousSolvedCount(solved);
            progress.setSolvedCount(solved);
            progress.setStrengthLevel(calculateStrength(solved));
            progress.setRevisionPriority(calculateRevisionPriority(solved));
            repo.save(progress);
        }
    }
    private void processTags(List<SkillTagDTO> tags,Map<CoreTopic,Integer> topicCounts){
        for(SkillTagDTO tag:tags){
            CoreTopic topic=TopicMapper.getTopic(tag.getTagName());
            if(topic==null)continue;
            int solved=tag.getProblemsSolved();
            topicCounts.put(topic,topicCounts.getOrDefault(topic,0)+solved);
        }
    }
    public void syncUserAnalytics(User user, String username){
        SkillResponseDTO response = leetcodeService.fetchSkill(username);
        processSkillStats(user,response);
    }
    public List<TopicAnalyticsResponseDTO> getUserAnalytics(User user){
        List<UserTopicProgress>progressList=repo.findByUser(user);
        return progressList.stream()
                .map(progress->TopicAnalyticsResponseDTO.builder()
                        .topic(progress.getTopic().name())
                        .solvedCount(progress.getSolvedCount())
                        .strengthLevel(progress.getStrengthLevel().name())
                        .revisionPriority(progress.getRevisionPriority())
                        .build()
                ).toList();
    }
    public List<RecommendationDTO>getRecommendations(User user){
        List<UserTopicProgress>progressList=repo.findByUser(user);
        List<RecommendationDTO> recommendations=new ArrayList<>();
        for(UserTopicProgress progress:progressList){
            if(progress.getStrengthLevel()==StrengthLevel.WEAK){
                recommendations.add(
                        RecommendationDTO.builder()
                                .topic(progress.getTopic().name())
                                .message(progress.getTopic().name()+"needs more practice")
                                .priority(2)
                                .build()
                );
            }
            if(progress.getDecayCounter()!=null&&progress.getDecayCounter()<=3){
                recommendations.add(
                        RecommendationDTO.builder()
                                .topic(progress.getTopic().name())
                                .message("You haven't revised " +progress.getTopic().name()+" recently")
                                .priority(1)
                                .build()
                );
            }
        }
        recommendations.sort(
                Comparator.comparing(RecommendationDTO::getPriority)
        );
        return recommendations;
    }

    public AnalyticsSummary getSummary(
            User user
    ) {

        String username =
                user.getLeetcodeUserName();

        SolvedStatsDTO solvedStats =
                leetcodeClient.fetchSolvedStats(
                        username
                );

        List<UserTopicProgress> progressList =
                repo.findByUser(user);

        int strongTopics = (int)
                progressList.stream()
                        .filter(progress ->
                                progress.getStrengthLevel()
                                        == StrengthLevel.STRONG
                        )
                        .count();

        int weakTopics = (int)
                progressList.stream()
                        .filter(progress ->
                                progress.getStrengthLevel()
                                        == StrengthLevel.WEAK
                        )
                        .count();

        int accepted =
                solvedStats.getAcSubmissionNum()[0]
                        .getSubmissions();

        int total =
                solvedStats.getTotalSubmissionNum()[0]
                        .getSubmissions();

        int acceptanceRate =
                total == 0
                        ? 0
                        : (accepted * 100) / total;

        return AnalyticsSummary.builder()
                .totalSolved(
                        solvedStats.getSolvedProblem()
                )
                .easySolved(
                        solvedStats.getEasySolved()
                )
                .mediumSolved(
                        solvedStats.getMediumSolved()
                )
                .hardSolved(
                        solvedStats.getHardSolved()
                )
                .acceptanceRate(
                        acceptanceRate
                )
                .strongTopics(
                        strongTopics
                )
                .weakTopics(
                        weakTopics
                )
                .build();
    }
}
