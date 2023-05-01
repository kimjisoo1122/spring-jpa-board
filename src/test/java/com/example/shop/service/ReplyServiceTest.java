package com.example.shop.service;

import com.example.shop.dto.BoardDTO;
import com.example.shop.dto.MemberDTO;
import com.example.shop.dto.ReplyDTO;
import com.example.shop.entity.Board;
import com.example.shop.entity.Member;
import com.example.shop.entity.Reply;
import com.example.shop.test.TestDataUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

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
    EntityManager em;

    @Test
    void 댓글등록() throws Exception {
        // given
        MemberDTO testMemberDTO = TestDataUtil.getTestMemberDTO();
        // when
        Long memberId = memberService.join(testMemberDTO);
        Member member = memberService.findById(memberId).orElse(null);
        // then
        assertNotNull(member);

        // given
        BoardDTO testBoardDTO = TestDataUtil.getTestBoardDTO();
        testBoardDTO.setMemberId(memberId);
        // when
        Long boardId = boardService.register(testBoardDTO);
        Board board = boardService.findById(boardId).orElse(null);
        // then
        assertNotNull(board);

        // given
        ReplyDTO replyDTO = new ReplyDTO();
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
        Board board2 = boardService.findById(boardId).orElse(null);
        System.out.println("board.getReplies() = " + board2.getReplies());

    }
}