package com.example.board.entity;

import lombok.Getter;

@Getter
public enum RecommendationStatus {
    UPVOTED, DOWNVOTED, NOT_VOTED;

    public RecommendationStatus getRecommendationStatus() {
        return this;
    }
}
