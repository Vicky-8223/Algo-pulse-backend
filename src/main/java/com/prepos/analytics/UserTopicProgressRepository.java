package com.prepos.analytics;


import com.prepos.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTopicProgressRepository extends JpaRepository<UserTopicProgress,Long> {
    List<UserTopicProgress> findByUser(User user);
    Optional<UserTopicProgress> findByUserAndTopic(User user,CoreTopic topic);

}
