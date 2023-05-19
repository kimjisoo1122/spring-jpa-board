package com.example.board.service;

import com.example.board.dto.board.BoardDto;
import com.example.board.dto.board.BoardSearchCondition;
import com.example.board.entity.*;
import com.example.board.entity.enums.RecommendationStatus;
import com.example.board.repository.board.BoardRecommendRepository;
import com.example.board.repository.board.BoardRepository;
import com.example.board.repository.board.BoardViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.example.board.entity.enums.RecommendationStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final BoardViewRepository boardViewRepository;
    private final BoardRecommendRepository boardRecommendRepository;

    public Optional<Board> findById(Long boardId) {
        return boardRepository.findById(boardId);
    }

    public BoardDto findBoardDtoById(Long boardId, Long memberId) {
        return findById(boardId)
                .map(board -> {
                    BoardDto boardDto = new BoardDto(board);
                    // 현재 게시글을 조회한 사용자의 추천이력 조회
                    boardRecommendRepository.findByMemberIdAndBoardId(memberId, boardId)
                            .ifPresent(recommendHistory -> boardDto.setRecommendationStatus(recommendHistory.getStatus()));
                    return boardDto;
                })
                .orElse(null);
    }

    public Long register(BoardDto boardDTO) {
        Member member = memberService.findById(boardDTO.getMemberId())
                .orElseThrow(() -> {
                    throw new NoSuchElementException("멤버가 조회되지 않습니다.");});
        Category category = categoryService.findByName(boardDTO.getCategoryName())
                .orElseThrow(() -> {
                    throw new NoSuchElementException("카테고리가 조회되지 않습니다.");});

        Board board = Board.createBoard(boardDTO);
        board.setMember(member);
        board.setCategory(category);
        boardRepository.save(board);
        return board.getId();
    }

    public Page<BoardDto> search(BoardSearchCondition boardSearchCondition, Pageable pageable) {
        return boardRepository.search(boardSearchCondition, pageable)
                .map(BoardDto::new);
    }

    public void increaseViewCnt(Long memberId, Long boardId) {
        Board board = findById(boardId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("게시글이 조회되지 않습니다.");
                });
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("멤버가 조회되지 않습니다.");
                });

        // 조회 이력 있는 경우 중복조회하지 않는다.
        if (isAlreadyViewed(memberId, boardId)) {
            return;
        }
        BoardViewHistory boardViewHistory = BoardViewHistory.createBoardViewHistory(board, member);
        boardViewRepository.save(boardViewHistory);

        board.increaseViewCnt();
    }

    private boolean isAlreadyViewed(Long memberId, Long boardId) {
        return boardViewRepository.findByMemberIdAndBoardId(memberId, boardId).isPresent();
    }

    public RecommendationStatus addRecommendation(Long memberId, Long boardId) {
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("멤버가 조회되지 않습니다.");
                });
        Board board = findById(boardId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("게시글이 조회되지 않습니다.");
                });

        BoardRecommendHistory boardRecommendHistory =
                boardRecommendRepository.findByMemberIdAndBoardId(memberId, boardId).orElse(null);

        if (boardRecommendHistory == null) {
            BoardRecommendHistory history = BoardRecommendHistory.createHistory(board, member, UP_VOTED);
            boardRecommendRepository.save(history);
            board.addRecommendation(1);
            return UP_VOTED;
        }

        if (boardRecommendHistory.getStatus() == NOT_VOTED) {
            boardRecommendHistory.updateStatus(UP_VOTED);
            board.addRecommendation(1);
        } else if (boardRecommendHistory.getStatus() == UP_VOTED) {
            boardRecommendHistory.updateStatus(NOT_VOTED);
            board.removeRecommendation(1);
        }

        return boardRecommendHistory.getStatus();
    }

    public RecommendationStatus removeRecommendation(Long memberId, Long boardId) {
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("멤버가 조회되지 않습니다.");
                });
        Board board = findById(boardId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("게시글이 조회되지 않습니다.");
                });

        BoardRecommendHistory boardRecommendHistory =
                boardRecommendRepository.findByMemberIdAndBoardId(memberId, boardId).orElse(null);

        if (boardRecommendHistory == null) {
            BoardRecommendHistory history = BoardRecommendHistory.createHistory(board, member, DOWN_VOTED);
            boardRecommendRepository.save(history);
            board.removeRecommendation(1);
            return DOWN_VOTED;
        }

        if (boardRecommendHistory.getStatus() == NOT_VOTED) {
            boardRecommendHistory.updateStatus(DOWN_VOTED);
            board.removeRecommendation(1);
        } else if (boardRecommendHistory.getStatus() == DOWN_VOTED) {
            boardRecommendHistory.updateStatus(NOT_VOTED);
            board.addRecommendation(1);
        }

        return boardRecommendHistory.getStatus();
    }

    public Long update(BoardDto boardDto) {
        Board board = findById(boardDto.getId())
                .orElseThrow(() -> {
                    throw new NoSuchElementException("게시글이 조회되지 않습니다.");
                });
        String categoryName = boardDto.getCategoryName();
        if (StringUtils.hasText(categoryName)) {
            categoryService.findByName(categoryName)
                    .ifPresent(board::setCategory);
        }
        board.update(boardDto);
        return board.getId();
    }

    public int delete(Long boardId, Long memberId) {
        return boardRepository.deleteByIdAndMemberId(boardId, memberId);
    }
}
