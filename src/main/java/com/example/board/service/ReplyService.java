package com.example.board.service;

import com.example.board.dto.ReplyDto;
import com.example.board.entity.Board;
import com.example.board.entity.Member;
import com.example.board.entity.Reply;
import com.example.board.repository.ReplyRepository;
import com.example.board.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final MemberService memberService;
    private final BoardRepository boardRepository;

    public Long register(ReplyDto replyDTO) {
        Board board = boardRepository.findById(replyDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("조회된 게시글 정보가 없습니다."));
        Member member = memberService.findById(replyDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("조회된 멤버 정보가 없습니다."));

        Reply reply = new Reply(replyDTO.getContent(), board, member);
        replyRepository.save(reply);
        return reply.getId();
    }

    public Optional<Reply> findById(Long replyId) {
        return replyRepository.findById(replyId);
    }
    public List<ReplyDto> findByBoardId(Long boardId) {
        return replyRepository.findByBoardId(boardId).stream()
                .map(ReplyDto::new)
                .collect(Collectors.toList());
    }
}
