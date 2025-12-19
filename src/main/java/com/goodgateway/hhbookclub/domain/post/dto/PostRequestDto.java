package com.goodgateway.hhbookclub.domain.post.dto;

import java.time.LocalDate;

public record PostRequestDto(
        String title,
        String content,
        String bookTitle,
        String bookAuthor,
        String bookCover,
        String bookPublisher,
        Integer rating,
        LocalDate startDate,
        LocalDate endDate) {
}
