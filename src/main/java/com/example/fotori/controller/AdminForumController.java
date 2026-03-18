package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.service.ForumThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/forum")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminForumController {

    private final ForumThreadService forumThreadService;

    @DeleteMapping("/threads/{id}")
    public ResponseEntity<ApiResponse> deleteThread(@PathVariable Long id) {

        forumThreadService.deleteThread(id);

        return ResponseEntity.ok(
            new ApiResponse(
                "SUCCESS",
                "Forum thread deleted",
                null
            )
        );
    }
}
