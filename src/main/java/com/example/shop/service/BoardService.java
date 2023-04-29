package com.example.shop.service;

import com.example.shop.dto.BoardDTO;
import com.example.shop.entity.Board;
import com.example.shop.entity.BoardViewHistory;
import com.example.shop.entity.Category;
import com.example.shop.entity.Member;
import com.example.shop.repository.BoardRepository;
import com.example.shop.repository.BoardViewHistoryRepository;
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
    private final BoardViewHistoryRepository boardViewHistoryRepository;

    public Optional<Board> findById(Long boardId) {
        return boardRepository.findById(boardId);
    }

    public BoardDTO findBoardDTOById(Long boardId) {
        return this.findById(boardId)
                .map(t -> {
                    BoardDTO boardDTO = new BoardDTO();
                    boardDTO.setId(t.getId());
                    boardDTO.setMemberId(t.getMember().getId());
                    boardDTO.setName(t.getMember().getName());
                    boardDTO.setTitle(t.getTitle());
                    boardDTO.setCategoryName(t.getCategory().getName());
                    boardDTO.setCategoryParentName(t.getCategory().getParent().getName());
                    boardDTO.setContent(t.getContent());
                    boardDTO.setViewCnt(t.getViewCnt());
                    boardDTO.setCreateDate(t.getCreateDate());
                    boardDTO.setRecommendCnt(t.getRecommendCnt());
                    return boardDTO;
                })
                .orElseThrow(() -> new IllegalArgumentException("게시글이 조회되지 않습니다."));
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
    public List<BoardDTO> findBoardList(int offset, int limit) {
        return boardRepository.findBoardList(offset, limit);
    }

    public void increaseViewCnt(Long boardId, Long memberId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 조회되지 않습니다."));
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("멤버가 조회되지 않습니다."));

        // 조회 이력 있는 경우 중복조회하지 않는다.
        if (isAlreadyViewed(boardId, memberId)) {
            return;
        }

        BoardViewHistory boardViewHistory = new BoardViewHistory(board, member);
        boardViewHistoryRepository.save(boardViewHistory);

        board.increaseViewCnt();
    }
    private boolean isAlreadyViewed(Long boardId, Long memberId) {
        return boardViewHistoryRepository.findByMemberIdAndBoardId(memberId, boardId).isPresent();
    }
}
