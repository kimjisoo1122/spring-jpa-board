package com.example.board.repository.board;

import com.example.board.entity.BoardViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardViewRepository extends JpaRepository<BoardViewHistory, Long> {

    Optional<BoardViewHistory> findByMemberIdAndBoardId(Long memberId, Long boardId);
}
