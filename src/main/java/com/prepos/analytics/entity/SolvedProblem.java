package com.prepos.analytics.entity;

import com.prepos.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Table
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolvedProblem{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String problemTitle;

    private String titleSlug;

    private String difficulty;

    private int submissionCount;

    private boolean SolvedInContest;

    private LocalDateTime solvedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private String language;

    private int likes;

    private int dislikes;

    private boolean paidOnly;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "problem_topics",
            joinColumns = @JoinColumn(
                    name = "problem_id"
            )
    )
    @Column(name = "topic")
    private List<String> topicTags;


}
