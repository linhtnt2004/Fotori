package com.example.fotori.repository;

import com.example.fotori.model.ForumReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumReplyRepository extends JpaRepository<ForumReply, Long> {

    List<ForumReply> findByThread_IdOrderByCreatedAtAsc(Long threadId);
}
