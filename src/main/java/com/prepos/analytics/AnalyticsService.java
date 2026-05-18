package com.prepos.analytics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prepos.analytics.dto.*;
import com.prepos.analytics.entity.SolvedProblem;
import com.prepos.analytics.entity.TopicWeaknessScore;
import com.prepos.analytics.entity.UserFeatureSnapshot;
import com.prepos.analytics.repository.SolvedProblemRepository;
import com.prepos.analytics.repository.TopicWeaknessScoreRepository;
import com.prepos.analytics.repository.UserFeatureSnapshotRepository;
import com.prepos.analytics.util.LeetcodeTagMapper;
import com.prepos.auth.entity.User;
import com.prepos.common.constants.AnalyticsConstants;
import com.prepos.integration.leetcode.client.LeetcodeClient;
import com.prepos.integration.leetcode.dto.*;
import com.prepos.integration.leetcode.service.LeetcodeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnalyticsService {
    private final UserTopicProgressRepository repo;
    private final LeetcodeService leetcodeService;
    private final LeetcodeClient leetcodeClient;
    private final ObjectMapper objectMapper;
    private final SolvedProblemRepository solvedProblemRepo;
    private final UserFeatureSnapshotRepository snapshotRepository;
    private final TopicWeaknessScoreRepository weaknessRepository;
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
//    public List<RecommendationDTO>getRecommendations(User user){
//        List<UserTopicProgress>progressList=repo.findByUser(user);
//        List<RecommendationDTO> recommendations=new ArrayList<>();
//        for(UserTopicProgress progress:progressList){
//            if(progress.getStrengthLevel()==StrengthLevel.WEAK){
//                recommendations.add(
//                        RecommendationDTO.builder()
//                                .topic(progress.getTopic().name())
//                                .message(progress.getTopic().name()+" needs more practice")
//                                .priority(2)
//                                .build()
//                );
//            }
//            if(progress.getDecayCounter()!=null&&progress.getDecayCounter()<=3){
//                recommendations.add(
//                        RecommendationDTO.builder()
//                                .topic(progress.getTopic().name())
//                                .message("You haven't revised " +progress.getTopic().name()+" recently")
//                                .priority(1)
//                                .build()
//                );
//            }
//        }
//        recommendations.sort(
//                Comparator.comparing(RecommendationDTO::getPriority)
//        );
//        return recommendations;
//    }
    @Cacheable(
            value="analyticsSummary",
            key="#user.id"
    )
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
    @Cacheable(
            value="calendarData",
            key="#user.leetcodeUserName"
    )
    public CalendarDTO getCalendar(User user)throws Exception{
        String username=user.getLeetcodeUserName();
        CalendarResponse response=leetcodeClient.getCalendar(username);
        String calendarJson=response.getSubmissionCalendar();
        Map<String,Integer>calendarMap=objectMapper.readValue(calendarJson,new TypeReference<Map<String,Integer>>(){});
        List<SubmissionDayDTO>submissionDays=new ArrayList<>();
        int totalSubmissions=0;
        int maxDailySubmissions=0;
        for(Map.Entry<String,Integer>entry:calendarMap.entrySet()){
            long timestamp=Long.parseLong(entry.getKey());
            int count=entry.getValue();
            String date= Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault())
                    .toLocalDate().toString();
            submissionDays.add(
                    SubmissionDayDTO.builder()
                            .date(date)
                            .count(count)
                            .build()
            );
            totalSubmissions+=count;
            maxDailySubmissions=Math.max(maxDailySubmissions,count);
        }
        double consistencyScore=((double)response.getTotalActiveDays()/365)*100;
        consistencyScore=Math.min(consistencyScore,100.0);
        consistencyScore=Math.round(consistencyScore*100)/100.0;
        submissionDays.sort(Comparator.comparing(SubmissionDayDTO::getDate));

        ConsistencyDTO consistencyDTO=ConsistencyDTO.builder()
                .streak(response.getStreak())
                .totalActiveDays(response.getTotalActiveDays())
                .totalSubmissions(totalSubmissions)
                .maxDailySubmissions(maxDailySubmissions)
                .consistencyScore(consistencyScore).build();

        return CalendarDTO.builder()
                .submissions(submissionDays)
                .consistency(consistencyDTO).build();
    }
    @Cacheable(
            value="difficultyAnalytics",
            key="#user.id"
    )
    public DifficultyAnalyticsDTO getDifficultyAnalytics(User user){
        String username=user.getLeetcodeUserName();
        SolvedStatsDTO solvedStatsDTO=leetcodeClient.fetchSolvedStats(username);
        int easy=solvedStatsDTO.getEasySolved();
        int medium=solvedStatsDTO.getMediumSolved();
        int hard=solvedStatsDTO.getHardSolved();
        int total=solvedStatsDTO.getSolvedProblem();
        double easyPercentage=total==0?0:((double)easy*100)/total;
        double mediumPercentage=total==0?0:((double)medium*100)/total;
        double hardPercentage=total==0?0:((double)hard*100)/total;
        easyPercentage=Math.round(easyPercentage*100)/100.0;
        mediumPercentage=Math.round(mediumPercentage*100)/100.0;
        hardPercentage=Math.round(hardPercentage*100)/100.0;
        return DifficultyAnalyticsDTO.builder()
                .easySolved(easy)
                .mediumSolved(medium)
                .hardSolved(hard)
                .easyPercentage(easyPercentage)
                .totalSolved(total)
                .mediumPercentage(mediumPercentage)
                .hardPercentage(hardPercentage).build();
    }

    @Transactional
    public void syncSolvedProblems(User user){
        String username=user.getLeetcodeUserName();
        AcSubmissionResponseDTO response= leetcodeClient.getAcceptedSubmissions(username);
        List<RecentSubmissionDTO>submissions=response.getSubmission();
        if(submissions==null){return;}
        for(RecentSubmissionDTO submission:submissions){
            boolean exists=solvedProblemRepo.existsByUserAndTitleSlug(user,submission.getTitleSlug());
            if(exists)continue;
            SolvedProblem problem=SolvedProblem.builder()
                    .problemTitle(submission.getTitle())
                    .titleSlug(submission.getTitleSlug())
                    .language(submission.getLang())
                    .solvedAt(Instant.ofEpochSecond(Long.parseLong(submission.getTimestamp())).atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .user(user)
                    .build();
            solvedProblemRepo.save(problem);
        }

    }
    @Transactional
    public void enrichSolvedProblems(User user){
        List<SolvedProblem> problems=solvedProblemRepo.findByUser(user);
        for(SolvedProblem problem:problems){
            if(problem.getDifficulty()!=null)continue;
            try{
                ProblemMetadataDTO metadata=leetcodeClient.getProblemMetadata(problem.getTitleSlug());
                problem.setDifficulty(metadata.getDifficulty());
                problem.setLikes(metadata.getLikes());
                problem.setDislikes(metadata.getDislikes());
                problem.setPaidOnly(metadata.isPaidOnly());
                List<String> topics=metadata.getTopicTags().stream()
                        .map(TopicTagDTO::getName)
                        .collect(Collectors.toList());
                problem.setTopicTags(topics);
                solvedProblemRepo.save(problem);
            }
            catch(Exception e){
                log.error(
                        "Failed to enrich {}",
                        problem.getTitleSlug(),
                        e
                );
            }
        }

    }

    @Transactional
    public void generateFeatureSnapshot(User user)throws Exception{
        LocalDate today=LocalDate.now();
        boolean exists=snapshotRepository.findByUserAndSnapShotDate(user,today).isPresent();
        if(exists)return;
        AnalyticsSummary summary=getSummary(user);
        CalendarDTO calendar=getCalendar(user);
        List<UserTopicProgress>topics=repo.findByUser(user);
        int strongTopics=(int)topics.stream().
                filter(t->t.getStrengthLevel()==StrengthLevel.STRONG).count();
        int weakTopics=(int)topics.stream().
                filter(t->t.getStrengthLevel()==StrengthLevel.WEAK).count();
        UserFeatureSnapshot snapshot=
                UserFeatureSnapshot.builder()
                        .snapShotDate(today)
                        .totalSolved(summary.getTotalSolved())
                        .easySolved(summary.getEasySolved())
                        .hardSolved(summary.getHardSolved())
                        .mediumSolved(summary.getMediumSolved())
                        .easyRatio(
                                summary.getTotalSolved()==0?0
                                        :(double)summary.getEasySolved()/summary.getTotalSolved()
                        )
                        .mediumRatio(
                                summary.getTotalSolved()==0?0
                                        :(double)summary.getMediumSolved()/summary.getTotalSolved()
                        )
                        .hardRatio(
                                summary.getTotalSolved()==0?0
                                        :(double)summary.getHardSolved()/summary.getTotalSolved()
                        )
                        .streak(calendar.getConsistency().getStreak())
                        .activeDays(calendar.getConsistency().getTotalActiveDays())
                        .consistencyScore(calendar.getConsistency().getConsistencyScore())
                        .totalSubmissions(calendar.getConsistency().getTotalSubmissions())
                        .maxDailySubmissions(calendar.getConsistency().getMaxDailySubmissions())
                        .strongTopicCount(strongTopics)
                        .weakTopicCount(weakTopics)
                        .user(user)
                        .build();
        snapshotRepository.save(snapshot);
    }
    @Transactional
    public void generateWeaknessScore(User user){
        List<UserTopicProgress>topics=repo.findByUser(user);
        for(UserTopicProgress topic:topics){
            double weakness=0;
            if(topic.getSolvedCount()<10)weakness+=40;
            else if(topic.getSolvedCount()<30)weakness+=25;
            else if(topic.getSolvedCount()<60)weakness+=10;

            weakness+=topic.getDecayCounter()*2;
            switch(topic.getStrengthLevel()){
                case WEAK:weakness+=35;
                case MEDIUM:weakness+=15;
                case STRONG:weakness+=0;
            }
            weakness=Math.min(100,weakness);
            double confidence=Math.min(100,topic.getSolvedCount()*2);
            int revisionDays;
            if(weakness>=80){
                revisionDays=1;
            }
            else if(weakness>=60)revisionDays=3;
            else if(weakness>=40)revisionDays=7;
            else revisionDays=14;

            TopicWeaknessScore entity=weaknessRepository.findByUserAndTopic(user,topic.getTopic()).orElse(new TopicWeaknessScore());
            entity.setUser(user);
            entity.setTopic(topic.getTopic());
            entity.setWeaknessScore(weakness);
            entity.setConfidenceScore(confidence);
            entity.setRecommendedRevisionDays(revisionDays);
            entity.setUpdatedAt(LocalDateTime.now());

            weaknessRepository.save(entity);

        }
    }
    public List<RecommendedProblemDTO>getRecommendations(User user){
        List<TopicWeaknessScore>weakTopics=weaknessRepository.findAll()
                .stream().filter(w->w.getUser().getId().equals(user.getId()))
                .sorted(Comparator.comparing(TopicWeaknessScore::getWeaknessScore).reversed())
                .limit(3).toList();
        Set<String>solvedSlugs=solvedProblemRepo.findByUser(user)
                .stream().map(SolvedProblem::getTitleSlug).collect(Collectors.toSet());
        List<RecommendedProblemDTO>recommendations=new ArrayList<>();
        for(TopicWeaknessScore weak:weakTopics){
            ProblemListResponseDTO response=leetcodeClient.getProblemByTopic(LeetcodeTagMapper.getTag(weak.getTopic()),"MEDIUM");
            for(ProblemSummaryDTO p:response.getProblemsetQuestionList()){
                if(solvedSlugs.contains(p.getTitleSlug()))continue;
                RecommendedProblemDTO dto=RecommendedProblemDTO.builder()
                        .title(p.getTitle())
                        .titleSlug(p.getTitleSlug())
                        .difficulty(p.getDifficulty())
                        .topic(weak.getTopic().name())
                        .reason("Weak topic improvement")
                        .priorityScore(weak.getWeaknessScore())
                        .build();
                recommendations.add(dto);
            }
        }
        return recommendations.stream().limit(10).toList();

    }
    public List<WeaknessDTO> getWeaknessAnalytics(User user){
        return weaknessRepository.findAll().stream()
                .filter(w->w.getUser().getId().equals(user.getId()))
                .map(w->WeaknessDTO.builder()
                        .topic(w.getTopic().name())
                        .weaknessScore(w.getWeaknessScore())
                        .build())
                .toList();
    }
    public List<SolvedProblemDTO> getSolvedProblems(User user){
        List<SolvedProblem> problems=solvedProblemRepo.findByUser(user);
        problems.sort((a,b)->b.getSolvedAt().compareTo(a.getSolvedAt()));
        return problems.stream()
                .map(problem->SolvedProblemDTO.builder()
                        .problemTitle(problem.getProblemTitle())
                        .titleSlug(problem.getTitleSlug())
                        .difficulty(problem.getDifficulty())
                        .likes(problem.getLikes())
                        .solvedAt(problem.getSolvedAt())
                        .topicTags(problem.getTopicTags())
                        .build()
                ).toList();
    }
}
