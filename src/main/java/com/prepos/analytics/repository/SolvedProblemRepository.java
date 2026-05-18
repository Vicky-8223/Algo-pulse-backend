package com.prepos.analytics.repository;

import com.prepos.analytics.entity.SolvedProblem;
import com.prepos.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolvedProblemRepository extends JpaRepository<SolvedProblem, Integer> {
    List<SolvedProblem>findByUser(User user);
    boolean existsByUserAndTitleSlug(User user,String titleSlug);
}
