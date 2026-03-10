package com.example.fotori.service.impl;

import com.example.fotori.dto.CreateForumReplyRequest;
import com.example.fotori.dto.CreateForumThreadRequest;
import com.example.fotori.model.ForumCategory;
import com.example.fotori.model.ForumReply;
import com.example.fotori.model.ForumThread;
import com.example.fotori.model.User;
import com.example.fotori.repository.ForumCategoryRepository;
import com.example.fotori.repository.ForumReplyRepository;
import com.example.fotori.repository.ForumThreadRepository;
import com.example.fotori.repository.UserRepository;
import com.example.fotori.service.ForumThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ForumThreadServiceImpl implements ForumThreadService {

    private final ForumThreadRepository forumThreadRepository;
    private final ForumReplyRepository forumReplyRepository;
    private final UserRepository userRepository;
    private final ForumCategoryRepository forumCategoryRepository;

    @Override
    public Page<ForumThread> getThreads(
        int page,
        int size,
        String category
    ) {

        Pageable pageable =
            PageRequest.of(page, size, Sort.by("createdAt").descending());

        if (category != null) {
            return forumThreadRepository.findByCategory_Slug(
                category,
                pageable
            );
        }

        return forumThreadRepository.findAll(pageable);
    }

    @Override
    public Map<String, Object> getThreadDetail(Long id) {

        ForumThread thread = forumThreadRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("THREAD_NOT_FOUND"));

        List<ForumReply> replies =
            forumReplyRepository.findByThread_IdOrderByCreatedAtAsc(id);

        return Map.of(
            "data", thread,
            "replies", replies
        );
    }

    @Override
    public ForumThread createThread(String email, CreateForumThreadRequest request) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        ForumCategory category = forumCategoryRepository
            .findBySlug(request.getCategory())
            .orElseThrow(() -> new RuntimeException("CATEGORY_NOT_FOUND"));

        ForumThread thread = ForumThread.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .author(user)
            .category(category)
            .tags(request.getTags())
            .likes(0)
            .build();

        return forumThreadRepository.save(thread);
    }

    @Override
    public ForumReply createReply(
        String email,
        Long threadId,
        CreateForumReplyRequest request
    ) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        ForumThread thread = forumThreadRepository.findById(threadId)
            .orElseThrow(() -> new RuntimeException("THREAD_NOT_FOUND"));

        ForumReply reply = ForumReply.builder()
            .thread(thread)
            .author(user)
            .content(request.getContent())
            .likes(0)
            .accepted(false)
            .build();

        return forumReplyRepository.save(reply);
    }

    @Override
    public int likeThread(Long threadId) {

        ForumThread thread = forumThreadRepository.findById(threadId)
            .orElseThrow(() -> new RuntimeException("THREAD_NOT_FOUND"));

        int likes = thread.getLikes() == null ? 0 : thread.getLikes();

        thread.setLikes(likes + 1);

        forumThreadRepository.save(thread);

        return thread.getLikes();
    }

    @Override
    public int likeReply(Long replyId) {

        ForumReply reply = forumReplyRepository.findById(replyId)
            .orElseThrow(() -> new RuntimeException("REPLY_NOT_FOUND"));

        int likes = reply.getLikes() == null ? 0 : reply.getLikes();

        reply.setLikes(likes + 1);

        forumReplyRepository.save(reply);

        return reply.getLikes();
    }
}
