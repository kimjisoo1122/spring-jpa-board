package com.example.board.repository.reply;

import com.example.board.entity.ReplyRecommendHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyRecommendRepository extends JpaRepository<ReplyRecommendHistory, Long> {

    Optional<ReplyRecommendHistory> findByMemberIdAndReplyId(Long memberId, Long replyId);
}
