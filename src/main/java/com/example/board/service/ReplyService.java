package com.example.board.service;

import com.example.board.dto.ReplyDto;
import com.example.board.entity.Board;
import com.example.board.entity.Member;
import com.example.board.entity.Reply;
import com.example.board.entity.ReplyRecommendHistory;
import com.example.board.entity.enums.RecommendationStatus;
import com.example.board.repository.MemberRepository;
import com.example.board.repository.reply.ReplyRecommendRepository;
import com.example.board.repository.reply.ReplyRepository;
import com.example.board.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.board.entity.enums.RecommendationStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ReplyRecommendRepository replyRecommendRepository;

    public Long register(ReplyDto replyDTO) {
        Board board = boardRepository.findById(replyDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("조회된 게시글 정보가 없습니다."));
        Member member = memberRepository.findById(replyDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("조회된 멤버 정보가 없습니다."));

        Reply reply = Reply.createReply(replyDTO.getContent(), board, member);
        replyRepository.save(reply);
        return reply.getId();
    }

    public Optional<Reply> findById(Long replyId) {
        return replyRepository.findById(replyId);
    }

    public ReplyDto findReplyDtoById(Long replyId, Long memberId) {
        ReplyDto replyDto = replyRepository.findById(replyId)
                .map(ReplyDto::new)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 조회되지 않습니다."));

        replyRecommendRepository.findByMemberIdAndReplyId(memberId, replyId)
                .ifPresent(replyHistory -> replyDto.setRecommendationStatus(replyHistory.getStatus()));
        return replyDto;
    }

    public List<ReplyDto> findByBoardId(Long boardId, Long memberId) {
        return replyRepository.findByBoardIdOrderByCreateDateAsc(boardId).stream()
                .map(reply -> {
                    ReplyDto replyDto = new ReplyDto(reply);
                    replyRecommendRepository.findByMemberIdAndReplyId(memberId, replyDto.getId())
                            .ifPresent(replyHistory -> replyDto.setRecommendationStatus(replyHistory.getStatus()));
                    return replyDto;
                })
                .collect(Collectors.toList());
    }

    public RecommendationStatus addRecommendation(Long memberId, Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("조회된 게시글 정보가 없습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("조회된 멤버 정보가 없습니다."));

        ReplyRecommendHistory replyRecommendHistory =
                replyRecommendRepository.findByMemberIdAndReplyId(memberId, replyId).orElse(null);

        if (replyRecommendHistory == null) {
            ReplyRecommendHistory history = ReplyRecommendHistory.createHistory(reply, member, UP_VOTED);
            replyRecommendRepository.save(history);
            reply.addRecommendation(1);
            return UP_VOTED;
        }

        if (replyRecommendHistory.getStatus() == NOT_VOTED) {
            replyRecommendHistory.updateStatus(UP_VOTED);
            reply.addRecommendation(1);
        } else if (replyRecommendHistory.getStatus() == UP_VOTED) {
            replyRecommendHistory.updateStatus(NOT_VOTED);
            reply.removeRecommendation(1);
        }
        return replyRecommendHistory.getStatus();
    }

    public RecommendationStatus removeRecommendation(Long memberId, Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("조회된 게시글 정보가 없습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("조회된 멤버 정보가 없습니다."));

        ReplyRecommendHistory replyRecommendHistory =
                replyRecommendRepository.findByMemberIdAndReplyId(memberId, replyId).orElse(null);

        if (replyRecommendHistory == null) {
            ReplyRecommendHistory history = ReplyRecommendHistory.createHistory(reply, member, DOWN_VOTED);
            replyRecommendRepository.save(history);
            reply.removeRecommendation(1);
            return DOWN_VOTED;
        }

        if (replyRecommendHistory.getStatus() == NOT_VOTED) {
            replyRecommendHistory.updateStatus(DOWN_VOTED);
            reply.removeRecommendation(1);
        } else if (replyRecommendHistory.getStatus() == DOWN_VOTED) {
            replyRecommendHistory.updateStatus(NOT_VOTED);
            reply.addRecommendation(1);
        }
        return replyRecommendHistory.getStatus();
    }

    public int delete(Long replyId, Long memberId) {
        return replyRepository.deleteByIdAndMemberId(replyId, memberId);
    }

    public void update(ReplyDto replyDto, Long memberId) {
        Reply reply = replyRepository.findByIdAndMemberId(replyDto.getId(), memberId)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("조회된 댓글 정보가 없습니다.");
                });
        reply.update(replyDto);
    }
}
