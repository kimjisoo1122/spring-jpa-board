package com.example.board.entity.enums;

import lombok.Getter;

@Getter
public enum RecommendationStatus {
    UP_VOTED, DOWN_VOTED, NOT_VOTED;

    public RecommendationStatus getRecommendationStatus() {
        return this;
    }
}
