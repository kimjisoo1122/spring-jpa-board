package com.example.board.service;

import com.example.board.TestDataUtil;
import com.example.board.dto.MemberDto;
import com.example.board.dto.ReplyDto;
import com.example.board.dto.board.BoardDto;
import com.example.board.entity.Board;
import com.example.board.entity.Category;
import com.example.board.entity.Member;
import com.example.board.entity.Reply;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ReplyServiceTest {

    @Autowired
    ReplyService replyService;
    @Autowired
    TestDataUtil testDataUtil;
    @Autowired
    EntityManager em;
    BoardDto boardDto = TestDataUtil.getTestBoardDto();
    MemberDto memberDto = TestDataUtil.getTestMemberDto();

    @Test
    @DisplayName("댓글 등록")
    void register() throws Exception {
        // given
        Member member = testDataUtil.createTestMember(memberDto);
        Category category = testDataUtil.createTestCategory("질문", null);
        Board board = testDataUtil.createTestBoard(member, category, boardDto);

        // when
        ReplyDto replyDto = new ReplyDto();
        replyDto.setContent("댓글내용");
        replyDto.setBoardId(board.getId());
        replyDto.setMemberId(member.getId());
        Long replyId = replyService.register(replyDto);

        //then
        Reply reply = replyService.findById(replyId).orElse(null);
        assertThat(reply).isNotNull();
    }

    @Test
    @DisplayName("게시글의 댓글 조회")
    void findByBoardId() throws Exception {
        // given
        Member member = testDataUtil.createTestMember(memberDto);
        Category category = testDataUtil.createTestCategory("질문", null);
        Board board = testDataUtil.createTestBoard(member, category, boardDto);
        memberDto.setEmail("test@test.com");
        Member member2 = testDataUtil.createTestMember(memberDto);

        ReplyDto replyDto = new ReplyDto();
        replyDto.setContent("댓글내용1");
        replyDto.setBoardId(board.getId());
        replyDto.setMemberId(member.getId());
        ReplyDto replyDto2 = new ReplyDto();
        replyDto2.setContent("댓글내용2");
        replyDto2.setBoardId(board.getId());
        replyDto2.setMemberId(member2.getId());
        replyService.register(replyDto);
        replyService.register(replyDto2);

        // when
        em.flush();
        em.clear();
        List<ReplyDto> replies = replyService.findByBoardId(board.getId());

        // then
        assertThat(replies).hasSize(2);
    }
}