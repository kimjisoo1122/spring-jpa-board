package com.example.board.entity;

import com.example.board.entity.common.BaseEntity;
import com.example.board.entity.enums.RecommendationStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyRecommendHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_recommend_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    private Reply reply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private RecommendationStatus status;

    public static ReplyRecommendHistory createHistory(Reply reply, Member member, RecommendationStatus status) {
        ReplyRecommendHistory replyRecommendHistory = new ReplyRecommendHistory();
        replyRecommendHistory.reply = reply;
        replyRecommendHistory.member = member;
        replyRecommendHistory.status = status;
        return replyRecommendHistory;
    }
    public void updateStatus(RecommendationStatus status) {
        this.status = status;
    }
}