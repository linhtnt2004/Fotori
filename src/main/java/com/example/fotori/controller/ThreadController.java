package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.dto.CreateForumThreadRequest;
import com.example.fotori.model.ForumThread;
import com.example.fotori.service.ForumThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/public/forum")
@RequiredArgsConstructor
public class ThreadController {

    private final ForumThreadService forumThreadService;

    @GetMapping("/threads")
    public ResponseEntity<ApiResponse> getThreads(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String category
    ) {

        Page<ForumThread> threads =
            forumThreadService.getThreads(page, size, category);

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Forum threads",
                Map.of(
                    "data", threads.getContent(),
                    "total", threads.getTotalElements()
                )
            )
        );
    }

    @GetMapping("/threads/{id}")
    public ResponseEntity<ApiResponse> getThreadDetail(
        @PathVariable Long id
    ) {

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Thread detail",
                forumThreadService.getThreadDetail(id)
            )
        );
    }

    @PostMapping("/threads")
    public ResponseEntity<ApiResponse> createThread(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody CreateForumThreadRequest request
    ) {

        ForumThread thread = forumThreadService.createThread(
            userDetails.getUsername(),
            request
        );

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Thread created",
                thread
            )
        );
    }
}
