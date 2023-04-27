package com.example.shop.service;

import com.example.shop.dto.BoardDTO;
import com.example.shop.entity.Board;
import com.example.shop.entity.Category;
import com.example.shop.entity.Member;
import com.example.shop.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final CategoryService categoryService;

    public Optional<Board> findById(Long boardId) {
        return boardRepository.findById(boardId);
    }
    public Long register(BoardDTO boardDTO) {
        Member member = memberService.findById(boardDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("멤버가 조회되지 않습니다."));
        Category category = categoryService.findByName(boardDTO.getCategoryName())
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 조회되지 않습니다."));

        Board board = Board.createBoard(boardDTO, member, category);
        boardRepository.save(board);
        return board.getId();
    }
    public List<BoardDTO> getBoardList(int offset, int limit) {
        return boardRepository.findBoardList(offset, limit);
    }
}
