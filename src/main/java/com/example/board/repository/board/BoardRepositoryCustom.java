package com.example.board.repository.board;

import com.example.board.dto.board.BoardSearchCondition;
import com.example.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<Board> search(BoardSearchCondition condition, Pageable pageable);
}
