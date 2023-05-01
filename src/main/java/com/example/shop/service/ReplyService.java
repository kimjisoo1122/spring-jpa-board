package com.example.shop.service;

import com.example.shop.dto.ReplyDTO;
import com.example.shop.entity.Board;
import com.example.shop.entity.Member;
import com.example.shop.entity.Reply;
import com.example.shop.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardService boardService;
    private final MemberService memberService;

    public Long register(ReplyDTO replyDTO) {
        Board board = boardService.findById(replyDTO.getBoardId())
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
    public List<ReplyDTO> findFromBoardId(Long boardId) {
        return replyRepository.findByBoardId(boardId);
    }
}
