package com.example.board.entity;

import com.example.board.entity.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @CreatedDate
    private LocalDateTime recommendDate;

    @Transient
    @Override
    public LocalDateTime getCreateDate() {
        return null;
    }
    public BoardRecommendHistory(Board board, Member member, RecommendationStatus status) {
        this.board = board;
        this.member = member;
        this.status = status;
    }

    public void updateStauts(RecommendationStatus status) {
        this.status = status;
    }
}
