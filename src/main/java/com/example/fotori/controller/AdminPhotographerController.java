package com.example.fotori.controller;

import com.example.fotori.common.ApiResponse;
import com.example.fotori.common.enums.ErrorCode;
import com.example.fotori.dto.UpdateApprovalStatusRequest;
import com.example.fotori.model.PhotographerProfile;
import com.example.fotori.repository.PhotographerRepository;
import com.example.fotori.service.AdminPhotographerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/photographers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPhotographerController {

    private final AdminPhotographerService adminPhotographerService;
    private final PhotographerRepository photographerRepository;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllPhotographers() {
        List<PhotographerProfile> all = photographerRepository.findAll();
        List<Map<String, Object>> result = all.stream().map(p -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", p.getId());
            map.put("userId", p.getUser().getId());
            map.put("fullName", p.getUser().getFullName());
            map.put("email", p.getUser().getEmail());
            map.put("approvalStatus", p.getApprovalStatus().name());
            map.put("bio", p.getBio());
            map.put("experienceYears", p.getExperienceYears());
            map.put("createdAt", p.getCreatedAt());
            map.put("approvedAt", p.getApprovedAt());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(
            new ApiResponse(ErrorCode.SUCCESS.name(), "All photographers", result)
        );
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse> getPendingPhotographers() {
        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Pending photographers",
                adminPhotographerService.getPendingPhotographers()
            )
        );
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<ApiResponse> updateStatus(
        @PathVariable Long id,
        @RequestBody UpdateApprovalStatusRequest request
    ) {
        adminPhotographerService.updatePhotographerStatus(id, request);

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Photographer status updated",
                null
            )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePhotographer(@PathVariable Long id) {
        adminPhotographerService.deletePhotographer(id);

        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Photographer deleted successfully",
                null
            )
        );
    }

    @DeleteMapping("/{id}/hard-delete")
    public ResponseEntity<ApiResponse> deletePhotographerHard(@PathVariable Long id) {
        adminPhotographerService.deletePhotographerHard(id);
        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Photographer deleted permanently",
                null
            )
        );
    }

    @PatchMapping("/{id}/cover-image")
    public ResponseEntity<ApiResponse> updateCoverImage(
        @PathVariable Long id,
        @RequestBody String coverUrl
    ) {
        adminPhotographerService.updateCoverImage(id, coverUrl);
        return ResponseEntity.ok(
            new ApiResponse(
                ErrorCode.SUCCESS.name(),
                "Cover image updated successfully",
                null
            )
        );
    }
}
