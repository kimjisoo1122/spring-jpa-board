package com.example.board.service;

import com.example.board.dto.board.BoardDto;
import com.example.board.dto.board.BoardSearchCondition;
import com.example.board.entity.*;
import com.example.board.repository.board.BoardRecommendRepository;
import com.example.board.repository.board.BoardRepository;
import com.example.board.repository.board.BoardViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public BoardDto findBoardDtoById(Long boardId) {
        return boardRepository.findById(boardId)
                .map(BoardDto::new)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 조회되지 않습니다."));
    }

    public Long register(BoardDto boardDTO) {
        Member member = memberService.findById(boardDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("멤버가 조회되지 않습니다."));
        Category category = categoryService.findByName(boardDTO.getCategoryName())
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 조회되지 않습니다."));

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
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 조회되지 않습니다."));
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("멤버가 조회되지 않습니다."));

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

    public void addRecommendation(Long memberId, Long boardId) {
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("멤버가 조회되지 않습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 조회되지 않습니다."));

        BoardRecommendHistory boardRecommendHistory =
                boardRecommendRepository.findByMemberIdAndBoardId(memberId, boardId).orElse(null);

        if (boardRecommendHistory == null) {
            boardRecommendRepository.save(new BoardRecommendHistory(board, member, RecommendationStatus.UPVOTED));
            board.addRecommendation(1);
            return;
        }

        switch (boardRecommendHistory.getStatus()) {
            case NOT_VOTED:
                boardRecommendHistory.updateStauts(RecommendationStatus.UPVOTED);
                board.addRecommendation(1);
                break;
            case UPVOTED:
                boardRecommendHistory.updateStauts(RecommendationStatus.NOT_VOTED);
                board.removeRecommendation(1);
                break;
//            case DOWNVOTED:
//                boardRecommendHistory.updateStauts(RecommendationStatus.UPVOTED);
//                board.addRecommendation(2);
//                break;
        }
    }

    public void removeRecommendation(Long memberId, Long boardId) {
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("멤버가 조회되지 않습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 조회되지 않습니다."));

        BoardRecommendHistory boardRecommendHistory =
                boardRecommendRepository.findByMemberIdAndBoardId(memberId, boardId).orElse(null);

        if (boardRecommendHistory == null) {
            boardRecommendRepository.save(new BoardRecommendHistory(board, member, RecommendationStatus.DOWNVOTED));
            board.removeRecommendation(1);
            return;
        }

        switch (boardRecommendHistory.getStatus()) {
            case NOT_VOTED:
                boardRecommendHistory.updateStauts(RecommendationStatus.DOWNVOTED);
                board.removeRecommendation(1);
                break;
            case DOWNVOTED:
                boardRecommendHistory.updateStauts(RecommendationStatus.NOT_VOTED);
                board.addRecommendation(1);
                break;
//            case UPVOTED:
//                boardRecommendHistory.updateStauts(RecommendationStatus.DOWNVOTED);
//                board.removeRecommendation(2);
//                break;
        }
    }
}
