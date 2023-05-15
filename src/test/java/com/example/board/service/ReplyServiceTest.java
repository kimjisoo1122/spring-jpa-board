package com.example.board.service;

import com.example.board.dto.board.BoardDto;
import com.example.board.dto.MemberDto;
import com.example.board.dto.ReplyDto;
import com.example.board.entity.Board;
import com.example.board.entity.Member;
import com.example.board.entity.Reply;
import com.example.board.TestDataUtil;
import com.example.board.repository.board.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ReplyServiceTest {

    @Autowired
    ReplyService replyService;
    @Autowired
    BoardService boardService;
    @Autowired
    MemberService memberService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    EntityManager em;

    @Test
    void 댓글등록() throws Exception {
        // given
        MemberDto testMemberDto = TestDataUtil.getTestMemberDTO();
        // when
        Long memberId = memberService.join(testMemberDto);
        Member member = memberService.findById(memberId).orElse(null);
        // then
        assertNotNull(member);

        // given
        BoardDto testBoardDto = TestDataUtil.getTestBoardDTO();
        testBoardDto.setMemberId(memberId);
        // when
        Long boardId = boardService.register(testBoardDto);
        Board board = boardRepository.findById(boardId).orElse(null);
        // then
        assertNotNull(board);

        // given
        ReplyDto replyDTO = new ReplyDto();
        replyDTO.setContent("test");
        replyDTO.setBoardId(boardId);
        replyDTO.setMemberId(memberId);
        //when
        Long replyId = replyService.register(replyDTO);
        Reply reply = replyService.findById(replyId).orElse(null);
        //then
        assertNotNull(reply);
        em.flush();
        em.clear();
        Board board2 = boardRepository.findById(boardId).orElse(null);
        System.out.println("board.getReplies() = " + board2.getReplies());

    }
}