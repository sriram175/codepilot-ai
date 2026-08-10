package com.sriram.ai.codepilot_ai.repository;

import com.sriram.ai.codepilot_ai.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation,Long> {
    List<Conversation> findByRepositoryIdOrderByUpdatedAtDesc(Long repositoryId);
}
