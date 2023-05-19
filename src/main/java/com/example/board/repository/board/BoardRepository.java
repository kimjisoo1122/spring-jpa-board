package com.example.board.repository.board;

import com.example.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom{

    Optional<Board> findByIdAndMemberId(Long boardId, Long memberId);

    int deleteByIdAndMemberId(Long boardId, Long memberId);
}
