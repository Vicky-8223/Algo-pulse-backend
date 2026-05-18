package com.prepos.analytics.repository;

import com.prepos.analytics.entity.UserFeatureSnapshot;
import com.prepos.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface UserFeatureSnapshotRepository extends JpaRepository<UserFeatureSnapshot,Long> {
    Optional<UserFeatureSnapshot>findByUserAndSnapShotDate(User user, LocalDate snapShotDate);

}
