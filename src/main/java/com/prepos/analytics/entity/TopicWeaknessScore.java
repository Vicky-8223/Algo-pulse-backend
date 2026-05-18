package com.prepos.analytics.entity;

import com.prepos.analytics.CoreTopic;
import com.prepos.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="topic_weakness_score")
public class TopicWeaknessScore {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private CoreTopic topic;

    private double weaknessScore;

    private double confidenceScore;

    private int recommendedRevisionDays;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

}
