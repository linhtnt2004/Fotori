package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.model.ForumThread;
import com.example.fotori.service.ForumThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
