package com.prepos.analytics.repository;


import com.prepos.analytics.CoreTopic;
import com.prepos.analytics.entity.TopicWeaknessScore;
import com.prepos.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TopicWeaknessScoreRepository extends JpaRepository<TopicWeaknessScore,Long> {
    Optional<TopicWeaknessScore> findByUserAndTopic(User user, CoreTopic topic);
}
