package com.backend.sanfely.review.controller;

import com.backend.sanfely.review.dto.ReviewCreateRequestDto;
import com.backend.sanfely.review.dto.ReviewResponseDto;
import com.backend.sanfely.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@Valid @RequestBody ReviewCreateRequestDto dto) {
        ReviewResponseDto created = reviewService.createReview(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @PatchMapping("/{orderId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
        @PathVariable UUID orderId,
        @Valid @RequestBody ReviewCreateRequestDto dto
    ) {
        ReviewResponseDto updated = reviewService.updateReview(orderId, dto);
        return ResponseEntity.ok(updated);
    }
}