package com.example.board.entity;

import com.example.board.entity.common.BaseEntity;
import com.example.board.entity.enums.RecommendationStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class BoardRecommendHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_recommend_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private RecommendationStatus status;

    public static BoardRecommendHistory createHistory(Board board, Member member, RecommendationStatus status) {
        BoardRecommendHistory boardRecommendHistory = new BoardRecommendHistory();
        boardRecommendHistory.board = board;
        boardRecommendHistory.member = member;
        boardRecommendHistory.status = status;
        return boardRecommendHistory;
    }

    public void updateStatus(RecommendationStatus status) {
        this.status = status;
    }
}
