package com.prepos.analytics;

import com.prepos.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="user_topic_progress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTopicProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private CoreTopic topic;

    private int solvedCount;

    private Integer revisionPriority;

    private LocalDate lastPracticeDate;

    @Enumerated(EnumType.STRING)
    private StrengthLevel strengthLevel;

    private Integer previousSolvedCount;

    private Integer decayCounter;

    private LocalDate lastDecayUpdateDate;

}
