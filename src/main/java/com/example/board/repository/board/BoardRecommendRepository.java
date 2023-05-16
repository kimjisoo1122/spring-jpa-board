package com.example.board.repository.board;

import com.example.board.entity.BoardRecommendHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRecommendRepository extends JpaRepository<BoardRecommendHistory, Long> {

    Optional<BoardRecommendHistory> findByMemberIdAndBoardId(Long memberId, Long boardId);
}
