package com.example.board.service;

import com.example.board.TestDataUtil;
import com.example.board.dto.MemberDto;
import com.example.board.dto.ReplyDto;
import com.example.board.dto.board.BoardDto;
import com.example.board.entity.*;
import com.example.board.entity.enums.RecommendationStatus;
import com.example.board.repository.reply.ReplyRecommendRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.board.entity.enums.RecommendationStatus.*;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ReplyServiceTest {

    @Autowired
    ReplyService replyService;
    @Autowired
    ReplyRecommendRepository replyRecommendRepository;
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
        List<ReplyDto> replies = replyService.findByBoardId(board.getId(), member.getId());

        // 게시글에 등록된 리플 확인
        Board findBoard = em.find(Board.class, board.getId());

        // then
        assertThat(replies).hasSize(2);
        assertThat(findBoard.getReplies()).hasSize(2);
    }

    @Test
    @DisplayName("추천수 UP&DOWN")
    void recommend() throws Exception {
        // given
        Member member = testDataUtil.createTestMember(memberDto);
        Category category = testDataUtil.createTestCategory("질문", null);
        Board board = testDataUtil.createTestBoard(member, category, boardDto);

        ReplyDto replyDto = new ReplyDto();
        replyDto.setContent("댓글내용1");
        replyDto.setBoardId(board.getId());
        replyDto.setMemberId(member.getId());
        Long replyId = replyService.register(replyDto);

        Reply reply = replyService.findById(replyId).orElse(null);
        assertThat(reply).isNotNull();

        // when
        RecommendationStatus status = replyService.addRecommendation(member.getId(), replyId);
        ReplyRecommendHistory replyRecommendHistory =
                replyRecommendRepository.findByMemberIdAndReplyId(member.getId(), replyId).orElse(null);

        // then
        assertThat(replyRecommendHistory).isNotNull();
        assertThat(replyRecommendHistory.getStatus()).isEqualTo(UP_VOTED).isEqualTo(status)
                .isEqualTo(replyService.findReplyDtoById(replyId, member.getId()).getRecommendationStatus());
        assertThat(replyRecommendHistory.getMember()).isSameAs(member);
        assertThat(replyRecommendHistory.getReply()).isSameAs(reply);
        assertThat(reply.getRecommendCnt()).isEqualTo(1);

        RecommendationStatus status1 = replyService.addRecommendation(member.getId(), replyId);
        assertThat(replyRecommendHistory.getStatus()).isEqualTo(NOT_VOTED).isEqualTo(status1)
                .isEqualTo(replyService.findReplyDtoById(replyId, member.getId()).getRecommendationStatus());
        assertThat(reply.getRecommendCnt()).isEqualTo(0);

        RecommendationStatus status2 = replyService.removeRecommendation(member.getId(), replyId);
        assertThat(replyRecommendHistory.getStatus()).isEqualTo(DOWN_VOTED).isEqualTo(status2)
                .isEqualTo(replyService.findReplyDtoById(replyId, member.getId()).getRecommendationStatus());
        assertThat(reply.getRecommendCnt()).isEqualTo(-1);

        RecommendationStatus status3 = replyService.removeRecommendation(member.getId(), replyId);
        assertThat(replyRecommendHistory.getStatus()).isEqualTo(NOT_VOTED).isEqualTo(status3)
                .isEqualTo(replyService.findReplyDtoById(replyId, member.getId()).getRecommendationStatus());
        assertThat(reply.getRecommendCnt()).isEqualTo(0);
    }

    @Test
    @DisplayName("삭제")
    void delete() throws Exception {
        // given
        Member member = testDataUtil.createTestMember(memberDto);
        Category category = testDataUtil.createTestCategory("질문", null);
        Board board = testDataUtil.createTestBoard(member, category, boardDto);

        ReplyDto replyDto = new ReplyDto();
        replyDto.setContent("댓글내용1");
        replyDto.setBoardId(board.getId());
        replyDto.setMemberId(member.getId());
        Long replyId = replyService.register(replyDto);

        Reply reply = replyService.findById(replyId).orElse(null);
        assertThat(reply).isNotNull();

        // when
        RecommendationStatus status = replyService.addRecommendation(member.getId(), replyId);
        ReplyRecommendHistory replyRecommendHistory =
                replyRecommendRepository.findByMemberIdAndReplyId(member.getId(), replyId).orElse(null);

        // then
        assertThat(replyRecommendHistory).isNotNull();
        assertThat(replyRecommendHistory.getStatus()).isEqualTo(UP_VOTED).isEqualTo(status)
                .isEqualTo(replyService.findReplyDtoById(replyId, member.getId()).getRecommendationStatus());
        assertThat(replyRecommendHistory.getMember()).isSameAs(member);
        assertThat(replyRecommendHistory.getReply()).isSameAs(reply);
        assertThat(reply.getRecommendCnt()).isEqualTo(1);

        em.flush();
        em.clear();
        int cnt = replyService.delete(replyId, member.getId());

        assertThat(cnt).isEqualTo(1);
        assertThat(replyService.findById(replyId).orElse(null)).isNull();
        assertThat(replyRecommendRepository.findByMemberIdAndReplyId(member.getId(), replyId).orElse(null)).isNull();
    }

    @Test
    @DisplayName("업데이트")
    void update() throws Exception {
        // given
        Member member = testDataUtil.createTestMember(memberDto);
        Category category = testDataUtil.createTestCategory("질문", null);
        Board board = testDataUtil.createTestBoard(member, category, boardDto);

        ReplyDto replyDto = new ReplyDto();
        replyDto.setContent("댓글내용1");
        replyDto.setBoardId(board.getId());
        replyDto.setMemberId(member.getId());
        Long replyId = replyService.register(replyDto);

        Reply reply = replyService.findById(replyId).orElse(null);
        assertThat(reply).isNotNull();
        // when
        ReplyDto updateReplyDto = new ReplyDto();
        updateReplyDto.setContent("test");
        updateReplyDto.setId(replyId);
        replyService.update(updateReplyDto, member.getId());

        em.flush();
        em.clear();
        // then
        Reply findReply = replyService.findById(replyId).orElse(null);
        assertThat(findReply).isNotNull();
        assertThat(findReply.getId()).isEqualTo(replyId);
        assertThat(findReply.getContent()).isEqualTo(updateReplyDto.getContent());
    }
}