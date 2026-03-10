package com.example.fotori.service.impl;

import com.example.fotori.dto.CategoryResponse;
import com.example.fotori.model.PhotoConcept;
import com.example.fotori.repository.PhotoConceptRepository;
import com.example.fotori.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final PhotoConceptRepository photoConceptRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {

        List<PhotoConcept> concepts = photoConceptRepository.findAll();

        return concepts.stream()
            .map(c -> CategoryResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .description(c.getDescription())
                .build())
            .collect(Collectors.toList());
    }
}