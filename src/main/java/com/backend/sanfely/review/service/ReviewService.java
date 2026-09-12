package com.backend.sanfely.review.service;

import com.backend.sanfely.common.exception.ResourceNotFoundException;
import com.backend.sanfely.common.exception.ReviewNotAllowedException;
import com.backend.sanfely.common.exception.UnauthorizedActionException;
import com.backend.sanfely.order.domain.Order;
import com.backend.sanfely.order.domain.OrderStatus;
import com.backend.sanfely.order.repository.OrderRepository;
import com.backend.sanfely.review.domain.Review;
import com.backend.sanfely.review.dto.ReviewCreateRequestDto;
import com.backend.sanfely.review.dto.ReviewResponseDto;
import com.backend.sanfely.review.mapper.ReviewMapper;
import com.backend.sanfely.review.repository.ReviewRepository;
import com.backend.sanfely.user.domain.User;
import com.backend.sanfely.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponseDto createReview(ReviewCreateRequestDto dto) {
        Order order = orderRepository.findById(dto.orderId())
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + dto.orderId()));

        User client = userRepository.findById(dto.clientId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.clientId()));

        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new ReviewNotAllowedException("Cannot review an order that hasn't been delivered yet");
        }

        if (!order.getClient().getId().equals(client.getId())) {
            throw new UnauthorizedActionException("You can only review your own orders");
        }

        if (reviewRepository.existsByOrderId(order.getId())) {
            throw new ReviewNotAllowedException("This order has already been reviewed");
        }

        Review review = new Review();
        review.setOrder(order);
        review.setClient(client);
        review.setRating(dto.rating());
        review.setComment(dto.comment());

        Review saved = reviewRepository.save(review);
        return reviewMapper.toResponseDto(saved);
    }
    @Transactional
    public ReviewResponseDto updateReview(UUID orderId, ReviewCreateRequestDto dto) {
        Review review = reviewRepository.findByOrderId(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("No review found for order: " + orderId));

        // still verify ownership, same check as before
        if (!review.getClient().getId().equals(dto.clientId())) {
            throw new UnauthorizedActionException("You can only edit your own review");
        }

        review.setRating(dto.rating());
        review.setComment(dto.comment());

        Review saved = reviewRepository.save(review);
        return reviewMapper.toResponseDto(saved);
    }
}