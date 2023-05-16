package com.example.board.repository.board;

import com.example.board.TestDataUtil;
import com.example.board.dto.MemberDto;
import com.example.board.dto.board.BoardDto;
import com.example.board.entity.Board;
import com.example.board.entity.BoardViewHistory;
import com.example.board.entity.Category;
import com.example.board.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BoardViewRepositoryTest {

    @Autowired
    BoardViewRepository boardViewRepository;
    @Autowired
    TestDataUtil testDataUtil;

    BoardDto boardDto = TestDataUtil.getTestBoardDto();
    MemberDto memberDto = TestDataUtil.getTestMemberDto();

    @Test
    @DisplayName("조회기록 저장 및 조회")
    void save() throws Exception {
        // given
        Member member = testDataUtil.createTestMember(memberDto);
        Category category = testDataUtil.createTestCategory("질문", null);
        Board board = testDataUtil.createTestBoard(member, category, boardDto);

        // when
        BoardViewHistory boardViewHistory = BoardViewHistory.createBoardViewHistory(board, member);
        boardViewRepository.save(boardViewHistory);

        // then
        BoardViewHistory findBoardViewHistory = boardViewRepository.findByMemberIdAndBoardId(member.getId(), board.getId()).orElse(null);
        assertThat(findBoardViewHistory).isNotNull();
    }
}