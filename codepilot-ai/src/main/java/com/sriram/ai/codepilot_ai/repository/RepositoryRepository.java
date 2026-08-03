package com.sriram.ai.codepilot_ai.repository;

import com.sriram.ai.codepilot_ai.entity.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryRepository extends JpaRepository<Repository, Long> {
    Optional<Repository> findByUrl(String url);
}
