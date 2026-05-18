package com.prepos.analytics.entity;

import com.prepos.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="user_feature_snapshot")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserFeatureSnapshot {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private LocalDate snapShotDate;

    private int totalSolved;

    private int easySolved;

    private int mediumSolved;

    private int hardSolved;

    private double easyRatio;

    private double mediumRatio;

    private double hardRatio;

    private int streak;

    private int activeDays;

    private double consistencyScore;

    private int totalSubmissions;

    private int maxDailySubmissions;

    private int strongTopicCount;

    private int weakTopicCount;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

}
